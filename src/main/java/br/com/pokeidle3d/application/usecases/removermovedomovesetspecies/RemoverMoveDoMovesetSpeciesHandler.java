package br.com.pokeidle3d.application.usecases.removermovedomovesetspecies;

import br.com.pokeidle3d.application.events.UnidadeTrabalhoEventosDominio;
import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.exceptions.MoveNaoEncontradoException;
import br.com.pokeidle3d.domain.exceptions.SpeciesMoveNaoEncontradoException;
import br.com.pokeidle3d.domain.exceptions.SpeciesNaoEncontradaException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesMoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RemoverMoveDoMovesetSpeciesHandler implements RemoverMoveDoMovesetSpeciesUseCase {

    private final SpeciesRepository speciesRepository;
    private final MoveRepository moveRepository;
    private final SpeciesMoveRepository speciesMoveRepository;
    private final UnidadeTrabalhoEventosDominio unidadeTrabalhoEventosDominio;

    public RemoverMoveDoMovesetSpeciesHandler(
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
    public Void handle(RemoverMoveDoMovesetSpeciesCommand command) {
        if (speciesRepository.buscarPorId(command.speciesId()).isEmpty()) {
            throw new SpeciesNaoEncontradaException("Especie nao encontrada");
        }
        if (moveRepository.buscarPorId(command.moveId()).isEmpty()) {
            throw new MoveNaoEncontradoException("Movimento nao encontrado");
        }

        List<SpeciesMove> associations = speciesMoveRepository.listarPorSpeciesIdEMoveId(command.speciesId(), command.moveId());
        if (associations.isEmpty()) {
            throw new SpeciesMoveNaoEncontradoException("Move nao associado ao moveset da especie");
        }

        associations.forEach(speciesMove -> {
            speciesMove.registrarRemocao(command.correlationKey());
            speciesMoveRepository.remover(speciesMove);
        });
        unidadeTrabalhoEventosDominio.publicarEventosPendentes();
        return null;
    }
}
