package br.com.pokeidle3d.infra.integrations.pokeapi;

public class PokeApiIntegracaoException extends RuntimeException {

    public PokeApiIntegracaoException(String message) {
        super(message);
    }

    public PokeApiIntegracaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
