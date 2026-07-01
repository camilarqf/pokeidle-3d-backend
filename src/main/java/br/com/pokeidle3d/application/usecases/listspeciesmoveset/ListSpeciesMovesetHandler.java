package br.com.pokeidle3d.application.usecases.listmovesetdaspecies;

import br.com.pokeidle3d.domain.entities.Move;
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
public class ListSpeciesMovesetHandler implements ListSpeciesMovesetUseCase {

    private final SpeciesRepository speciesRepository;
    private final MoveRepository moveRepository;
    private final SpeciesMoveRepository speciesMoveRepository;

    public ListSpeciesMovesetHandler(
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
    public List<SpeciesMovesetItem> handle(ListSpeciesMovesetQuery query) {
        if (speciesRepository.buscarPorId(query.speciesId()).isEmpty()) {
            throw new SpeciesNotFoundException("Especie nao encontrada");
        }

        return speciesMoveRepository.listarPorSpeciesId(query.speciesId())
                .stream()
                .map(this::carregarMove)
                .toList();
    }

    private SpeciesMovesetItem carregarMove(SpeciesMove speciesMove) {
        Move move = moveRepository.buscarPorId(speciesMove.getMoveId())
                .orElseThrow(() -> new MoveNotFoundException("Movimento nao encontrado"));
        return new SpeciesMovesetItem(speciesMove, move);
    }
}
