package br.com.pokeidle3d.infra.integrations.pokeapi;

import java.util.List;

public record PokeApiPokemonResponse(
        Integer id,
        String name,
        List<PokeApiTypeResponse> types,
        List<PokeApiStatResponse> stats,
        PokeApiSpriteResponse sprites
) {
}
