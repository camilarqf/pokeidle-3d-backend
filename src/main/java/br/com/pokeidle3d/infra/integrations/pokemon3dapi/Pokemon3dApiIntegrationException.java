package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

public class Pokemon3dApiIntegrationException extends RuntimeException {

    public Pokemon3dApiIntegrationException(String message) {
        super(message);
    }

    public Pokemon3dApiIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
