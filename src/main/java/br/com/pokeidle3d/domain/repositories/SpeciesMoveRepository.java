package br.com.pokeidle3d.domain.repositories;

import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;

import java.util.List;
import java.util.Optional;

public interface SpeciesMoveRepository {

    SpeciesMove salvar(SpeciesMove speciesMove);

    void remover(SpeciesMove speciesMove);

    Optional<SpeciesMove> buscarPorId(Long id);

    Optional<SpeciesMove> buscarPorChave(Long speciesId, Long moveId, MoveLearnMethod learnMethod, Integer levelLearnedAt);

    Optional<SpeciesMove> buscarPrimeiroPorSpeciesIdEMoveId(Long speciesId, Long moveId);

    List<SpeciesMove> listarPorSpeciesIdEMoveId(Long speciesId, Long moveId);

    List<SpeciesMove> listarPorSpeciesId(Long speciesId);

    List<SpeciesMove> listarPorMoveId(Long moveId);

    boolean existePorChave(Long speciesId, Long moveId, MoveLearnMethod learnMethod, Integer levelLearnedAt);
}
