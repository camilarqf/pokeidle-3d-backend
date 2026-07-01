package br.com.pokeidle3d.application.usecases.listspeciespormove;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.exceptions.MoveNotFoundException;
import br.com.pokeidle3d.domain.exceptions.SpeciesNotFoundException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesMoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListSpeciesByMoveHandler implements ListSpeciesByMoveUseCase {

    private final SpeciesRepository speciesRepository;
    private final MoveRepository moveRepository;
    private final SpeciesMoveRepository speciesMoveRepository;

    public ListSpeciesByMoveHandler(
            SpeciesRepository speciesRepository,
            MoveRepository moveRepository,
            SpeciesMoveRepository speciesMoveRepository
    ) {
        this.speciesRepository = speciesRepository;
        this.moveRepository = moveRepository;
        this.speciesMoveRepository = speciesMoveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpeciesByMoveItem> handle(ListSpeciesByMoveQuery query) {
        if (moveRepository.buscarPorId(query.moveId()).isEmpty()) {
            throw new MoveNotFoundException("Movimento nao encontrado");
        }

        return speciesMoveRepository.listarPorMoveId(query.moveId())
                .stream()
                .map(this::carregarSpecies)
                .toList();
    }

    private SpeciesByMoveItem carregarSpecies(SpeciesMove speciesMove) {
        Species species = speciesRepository.buscarPorId(speciesMove.getSpeciesId())
                .orElseThrow(() -> new SpeciesNotFoundException("Especie nao encontrada"));
        return new SpeciesByMoveItem(speciesMove, species);
    }
}
