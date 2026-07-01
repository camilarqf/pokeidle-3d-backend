package br.com.pokeidle3d.api.contracts;

import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddMoveToSpeciesMovesetRequest(
        @NotNull
        @Positive
        Long moveId,

        @NotNull
        MoveLearnMethod learnMethod,

        @Positive
        Integer levelLearnedAt
) {

    @AssertTrue(message = "levelLearnedAt e obrigatorio quando learnMethod = LEVEL_UP")
    public boolean isLevelLearnedAtValidoParaLevelUp() {
        return learnMethod != MoveLearnMethod.LEVEL_UP || levelLearnedAt != null;
    }
}
