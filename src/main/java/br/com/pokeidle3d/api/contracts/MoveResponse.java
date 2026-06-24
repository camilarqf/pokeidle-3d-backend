package br.com.pokeidle3d.api.contracts;

import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;

import java.time.Instant;

public record MoveResponse(
        Long id,
        String name,
        PokemonType type,
        Integer power,
        Integer accuracy,
        MoveCategory category,
        Integer pp,
        Instant createdAt,
        Instant updatedAt
) {
}
