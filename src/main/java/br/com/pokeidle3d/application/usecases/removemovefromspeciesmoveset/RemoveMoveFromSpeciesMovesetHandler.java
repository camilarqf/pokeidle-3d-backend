package br.com.pokeidle3d.application.usecases.removemovefromspeciesmoveset;

import br.com.pokeidle3d.application.events.DomainEventUnitOfWork;
import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.exceptions.MoveNotFoundException;
import br.com.pokeidle3d.domain.exceptions.SpeciesMoveNotFoundException;
import br.com.pokeidle3d.domain.exceptions.SpeciesNotFoundException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesMoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RemoveMoveFromSpeciesMovesetHandler implements RemoveMoveFromSpeciesMovesetUseCase {

    private final SpeciesRepository speciesRepository;
    private final MoveRepository moveRepository;
    private final SpeciesMoveRepository speciesMoveRepository;
    private final DomainEventUnitOfWork unidadeTrabalhoEventosDominio;

    public RemoveMoveFromSpeciesMovesetHandler(
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
    public Void handle(RemoveMoveFromSpeciesMovesetCommand command) {
        if (speciesRepository.buscarPorId(command.speciesId()).isEmpty()) {
            throw new SpeciesNotFoundException("Especie nao encontrada");
        }
        if (moveRepository.buscarPorId(command.moveId()).isEmpty()) {
            throw new MoveNotFoundException("Movimento nao encontrado");
        }

        List<SpeciesMove> associations = speciesMoveRepository.listarPorSpeciesIdEMoveId(command.speciesId(), command.moveId());
        if (associations.isEmpty()) {
            throw new SpeciesMoveNotFoundException("Move nao associado ao moveset da especie");
        }

        associations.forEach(speciesMove -> {
            speciesMove.registrarRemocao(command.correlationKey());
            speciesMoveRepository.remover(speciesMove);
        });
        unidadeTrabalhoEventosDominio.publicarEventosPendentes();
        return null;
    }
}
