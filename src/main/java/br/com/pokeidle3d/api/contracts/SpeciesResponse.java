package br.com.pokeidle3d.api.contracts;

import br.com.pokeidle3d.domain.valueobjects.PokemonType;

import java.time.Instant;

public record SpeciesResponse(
        Long id,
        Integer pokedexNumber,
        String name,
        PokemonType primaryType,
        PokemonType secondaryType,
        Integer baseHp,
        Integer baseAttack,
        Integer baseDefense,
        Integer baseSpecialAttack,
        Integer baseSpecialDefense,
        Integer baseSpeed,
        String spriteRef,
        String model3dRef,
        Instant createdAt,
        Instant updatedAt
) {
}
