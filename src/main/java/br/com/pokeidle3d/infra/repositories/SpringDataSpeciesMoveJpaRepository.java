package br.com.pokeidle3d.infra.repositories;

import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;
import br.com.pokeidle3d.infra.persistence.SpeciesMoveJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataSpeciesMoveJpaRepository extends JpaRepository<SpeciesMoveJpaEntity, Long> {

    Optional<SpeciesMoveJpaEntity> findBySpeciesIdAndMoveIdAndLearnMethodAndLevelLearnedAt(
            Long speciesId,
            Long moveId,
            MoveLearnMethod learnMethod,
            Integer levelLearnedAt
    );

    Optional<SpeciesMoveJpaEntity> findFirstBySpeciesIdAndMoveIdOrderByIdAsc(Long speciesId, Long moveId);

    List<SpeciesMoveJpaEntity> findBySpeciesIdAndMoveIdOrderByIdAsc(Long speciesId, Long moveId);

    List<SpeciesMoveJpaEntity> findBySpeciesIdOrderByMoveIdAscLearnMethodAscLevelLearnedAtAsc(Long speciesId);

    List<SpeciesMoveJpaEntity> findByMoveIdOrderBySpeciesIdAscLearnMethodAscLevelLearnedAtAsc(Long moveId);

    boolean existsBySpeciesIdAndMoveIdAndLearnMethodAndLevelLearnedAt(
            Long speciesId,
            Long moveId,
            MoveLearnMethod learnMethod,
            Integer levelLearnedAt
    );
}
