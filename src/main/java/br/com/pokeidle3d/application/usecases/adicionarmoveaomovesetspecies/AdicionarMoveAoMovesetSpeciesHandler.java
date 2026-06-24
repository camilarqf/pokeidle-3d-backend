package br.com.pokeidle3d.application.usecases.adicionarmoveaomovesetspecies;

import br.com.pokeidle3d.application.events.UnidadeTrabalhoEventosDominio;
import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.exceptions.MoveNaoEncontradoException;
import br.com.pokeidle3d.domain.exceptions.SpeciesMoveDuplicadoException;
import br.com.pokeidle3d.domain.exceptions.SpeciesNaoEncontradaException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesMoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdicionarMoveAoMovesetSpeciesHandler implements AdicionarMoveAoMovesetSpeciesUseCase {

    private final SpeciesRepository speciesRepository;
    private final MoveRepository moveRepository;
    private final SpeciesMoveRepository speciesMoveRepository;
    private final UnidadeTrabalhoEventosDominio unidadeTrabalhoEventosDominio;

    public AdicionarMoveAoMovesetSpeciesHandler(
            SpeciesRepository speciesRepository,
            MoveRepository moveRepository,
            SpeciesMoveRepository speciesMoveRepository,
            UnidadeTrabalhoEventosDominio unidadeTrabalhoEventosDominio
    ) {
        this.speciesRepository = speciesRepository;
        this.moveRepository = moveRepository;
        this.speciesMoveRepository = speciesMoveRepository;
        this.unidadeTrabalhoEventosDominio = unidadeTrabalhoEventosDominio;
    }

    @Override
    @Transactional
    public SpeciesMove handle(AdicionarMoveAoMovesetSpeciesCommand command) {
        if (speciesRepository.buscarPorId(command.speciesId()).isEmpty()) {
            throw new SpeciesNaoEncontradaException("Especie nao encontrada");
        }
        if (moveRepository.buscarPorId(command.moveId()).isEmpty()) {
            throw new MoveNaoEncontradoException("Movimento nao encontrado");
        }
        if (speciesMoveRepository.existePorChave(
                command.speciesId(),
                command.moveId(),
                command.learnMethod(),
                normalizarLevel(command)
        )) {
            throw new SpeciesMoveDuplicadoException("Move ja associado ao moveset da especie");
        }

        SpeciesMove speciesMove = SpeciesMove.adicionar(
                command.speciesId(),
                command.moveId(),
                command.learnMethod(),
                command.levelLearnedAt(),
                command.correlationKey()
        );

        SpeciesMove salvo = speciesMoveRepository.salvar(speciesMove);
        unidadeTrabalhoEventosDominio.publicarEventosPendentes();
        return salvo;
    }

    private Integer normalizarLevel(AdicionarMoveAoMovesetSpeciesCommand command) {
        return command.learnMethod() == MoveLearnMethod.LEVEL_UP ? command.levelLearnedAt() : null;
    }
}
