package br.com.pokeidle3d.api.contracts;

import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;

import java.time.Instant;

public record SpeciesPorMoveResponse(
        Long id,
        Long moveId,
        SpeciesResponse species,
        MoveLearnMethod learnMethod,
        Integer levelLearnedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
