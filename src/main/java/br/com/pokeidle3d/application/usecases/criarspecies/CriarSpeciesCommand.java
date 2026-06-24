package br.com.pokeidle3d.application.usecases.criarspecies;

import br.com.pokeidle3d.application.bus.Command;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
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
        String model3dRef,
        CorrelationKey correlationKey
) implements Command<Species> {
}
