package br.com.pokeidle3d.infra.repositories;

import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.infra.persistence.MoveJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataMoveJpaRepository extends JpaRepository<MoveJpaEntity, Long> {

    Optional<MoveJpaEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    Page<MoveJpaEntity> findByType(PokemonType type, Pageable pageable);

    Page<MoveJpaEntity> findByCategory(MoveCategory category, Pageable pageable);
}
