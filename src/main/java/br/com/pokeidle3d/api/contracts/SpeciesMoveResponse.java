package br.com.pokeidle3d.api.contracts;

import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;

import java.time.Instant;

public record SpeciesMoveResponse(
        Long id,
        Long speciesId,
        Long moveId,
        MoveLearnMethod learnMethod,
        Integer levelLearnedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
