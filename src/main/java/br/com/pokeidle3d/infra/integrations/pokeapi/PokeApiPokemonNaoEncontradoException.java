package br.com.pokeidle3d.infra.integrations.pokeapi;

public class PokeApiPokemonNaoEncontradoException extends PokeApiIntegracaoException {

    public PokeApiPokemonNaoEncontradoException(String idOrName) {
        super("Pokemon nao encontrado na PokeAPI: " + idOrName);
    }
}
