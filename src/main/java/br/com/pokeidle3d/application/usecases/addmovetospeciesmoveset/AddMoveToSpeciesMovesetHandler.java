package br.com.pokeidle3d.application.usecases.addmovetospeciesmoveset;

import br.com.pokeidle3d.application.events.DomainEventUnitOfWork;
import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.exceptions.MoveNotFoundException;
import br.com.pokeidle3d.domain.exceptions.DuplicateSpeciesMoveException;
import br.com.pokeidle3d.domain.exceptions.SpeciesNotFoundException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesMoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddMoveToSpeciesMovesetHandler implements AddMoveToSpeciesMovesetUseCase {

    private final SpeciesRepository speciesRepository;
    private final MoveRepository moveRepository;
    private final SpeciesMoveRepository speciesMoveRepository;
    private final DomainEventUnitOfWork unidadeTrabalhoEventosDominio;

    public AddMoveToSpeciesMovesetHandler(
            SpeciesRepository speciesRepository,
            MoveRepository moveRepository,
            SpeciesMoveRepository speciesMoveRepository,
            DomainEventUnitOfWork unidadeTrabalhoEventosDominio
    ) {
        this.speciesRepository = speciesRepository;
        this.moveRepository = moveRepository;
        this.speciesMoveRepository = speciesMoveRepository;
        this.unidadeTrabalhoEventosDominio = unidadeTrabalhoEventosDominio;
    }

    @Override
    @Transactional
    public SpeciesMove handle(AddMoveToSpeciesMovesetCommand command) {
        if (speciesRepository.buscarPorId(command.speciesId()).isEmpty()) {
            throw new SpeciesNotFoundException("Especie nao encontrada");
        }
        if (moveRepository.buscarPorId(command.moveId()).isEmpty()) {
            throw new MoveNotFoundException("Movimento nao encontrado");
        }
        if (speciesMoveRepository.existePorChave(
                command.speciesId(),
                command.moveId(),
                command.learnMethod(),
                normalizarLevel(command)
        )) {
            throw new DuplicateSpeciesMoveException("Move ja associado ao moveset da especie");
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

    private Integer normalizarLevel(AddMoveToSpeciesMovesetCommand command) {
        return command.learnMethod() == MoveLearnMethod.LEVEL_UP ? command.levelLearnedAt() : null;
    }
}
