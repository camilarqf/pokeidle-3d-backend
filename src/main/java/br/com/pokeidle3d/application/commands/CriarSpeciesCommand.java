package br.com.pokeidle3d.application.commands;

import br.com.pokeidle3d.domain.valueobjects.PokemonType;

public record CriarSpeciesCommand(
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
        String model3dRef
) {
}
