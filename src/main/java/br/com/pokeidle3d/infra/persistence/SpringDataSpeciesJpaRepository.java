package br.com.pokeidle3d.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataSpeciesJpaRepository extends JpaRepository<SpeciesJpaEntity, Long> {

    Optional<SpeciesJpaEntity> findByPokedexNumber(Integer pokedexNumber);

    boolean existsByPokedexNumber(Integer pokedexNumber);

    boolean existsByNameIgnoreCase(String name);
}
