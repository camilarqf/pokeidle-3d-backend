package br.com.pokeidle3d.api.contracts;

import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMoveRequest(
        @NotBlank String name,
        @NotNull PokemonType type,
        @Min(0) Integer power,
        @Min(1) @Max(100) Integer accuracy,
        @NotNull MoveCategory category,
        @NotNull @Min(1) Integer pp
) {
}
