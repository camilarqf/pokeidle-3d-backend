package br.com.pokeidle3d.application.usecases.listarmovesetdaspecies;

import br.com.pokeidle3d.domain.entities.Move;
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
public class ListarMovesetDaSpeciesHandler implements ListarMovesetDaSpeciesUseCase {

    private final SpeciesRepository speciesRepository;
    private final MoveRepository moveRepository;
    private final SpeciesMoveRepository speciesMoveRepository;

    public ListarMovesetDaSpeciesHandler(
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
    public List<MovesetSpeciesItem> handle(ListarMovesetDaSpeciesQuery query) {
        if (speciesRepository.buscarPorId(query.speciesId()).isEmpty()) {
            throw new SpeciesNaoEncontradaException("Especie nao encontrada");
        }

        return speciesMoveRepository.listarPorSpeciesId(query.speciesId())
                .stream()
                .map(this::carregarMove)
                .toList();
    }

    private MovesetSpeciesItem carregarMove(SpeciesMove speciesMove) {
        Move move = moveRepository.buscarPorId(speciesMove.getMoveId())
                .orElseThrow(() -> new MoveNaoEncontradoException("Movimento nao encontrado"));
        return new MovesetSpeciesItem(speciesMove, move);
    }
}
