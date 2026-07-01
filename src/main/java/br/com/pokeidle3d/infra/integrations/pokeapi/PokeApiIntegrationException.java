package br.com.pokeidle3d.infra.integrations.pokeapi;

public class PokeApiIntegrationException extends RuntimeException {

    public PokeApiIntegrationException(String message) {
        super(message);
    }

    public PokeApiIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
