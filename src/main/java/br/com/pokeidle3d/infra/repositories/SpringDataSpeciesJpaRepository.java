package br.com.pokeidle3d.infra.repositories;

import br.com.pokeidle3d.infra.persistence.SpeciesJpaEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataSpeciesJpaRepository extends JpaRepository<SpeciesJpaEntity, Long> {

    Optional<SpeciesJpaEntity> findByPokedexNumber(Integer pokedexNumber);

    Optional<SpeciesJpaEntity> findByNameIgnoreCase(String name);

    boolean existsByPokedexNumber(Integer pokedexNumber);

    boolean existsByNameIgnoreCase(String name);
}
