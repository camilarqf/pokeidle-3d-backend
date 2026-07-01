package br.com.pokeidle3d.infra.integrations.pokeapi;

public class PokeApiPokemonNotFoundException extends PokeApiIntegrationException {

    public PokeApiPokemonNotFoundException(String idOrName) {
        super("Pokemon nao encontrado na PokeAPI: " + idOrName);
    }
}
