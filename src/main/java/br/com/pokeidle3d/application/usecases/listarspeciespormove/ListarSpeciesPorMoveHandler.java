package br.com.pokeidle3d.application.usecases.listarspeciespormove;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.exceptions.MoveNaoEncontradoException;
import br.com.pokeidle3d.domain.exceptions.SpeciesNaoEncontradaException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesMoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListarSpeciesPorMoveHandler implements ListarSpeciesPorMoveUseCase {

    private final SpeciesRepository speciesRepository;
    private final MoveRepository moveRepository;
    private final SpeciesMoveRepository speciesMoveRepository;

    public ListarSpeciesPorMoveHandler(
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
    public List<SpeciesPorMoveItem> handle(ListarSpeciesPorMoveQuery query) {
        if (moveRepository.buscarPorId(query.moveId()).isEmpty()) {
            throw new MoveNaoEncontradoException("Movimento nao encontrado");
        }

        return speciesMoveRepository.listarPorMoveId(query.moveId())
                .stream()
                .map(this::carregarSpecies)
                .toList();
    }

    private SpeciesPorMoveItem carregarSpecies(SpeciesMove speciesMove) {
        Species species = speciesRepository.buscarPorId(speciesMove.getSpeciesId())
                .orElseThrow(() -> new SpeciesNaoEncontradaException("Especie nao encontrada"));
        return new SpeciesPorMoveItem(speciesMove, species);
    }
}
