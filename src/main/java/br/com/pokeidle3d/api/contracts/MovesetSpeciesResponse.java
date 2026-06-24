package br.com.pokeidle3d.api.contracts;

import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;

import java.time.Instant;

public record MovesetSpeciesResponse(
        Long id,
        Long speciesId,
        MoveResponse move,
        MoveLearnMethod learnMethod,
        Integer levelLearnedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
