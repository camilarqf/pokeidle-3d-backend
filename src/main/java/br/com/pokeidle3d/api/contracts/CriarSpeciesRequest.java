package br.com.pokeidle3d.api.contracts;

import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarSpeciesRequest(
        @NotNull @Min(1) Integer pokedexNumber,
        @NotBlank String name,
        @NotNull PokemonType primaryType,
        PokemonType secondaryType,
        @NotNull @Min(0) Integer baseHp,
        @NotNull @Min(0) Integer baseAttack,
        @NotNull @Min(0) Integer baseDefense,
        @NotNull @Min(0) Integer baseSpecialAttack,
        @NotNull @Min(0) Integer baseSpecialDefense,
        @NotNull @Min(0) Integer baseSpeed,
        String spriteRef,
        String model3dRef
) {
}
