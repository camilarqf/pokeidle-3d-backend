package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

public class Pokemon3dApiIntegracaoException extends RuntimeException {

    public Pokemon3dApiIntegracaoException(String message) {
        super(message);
    }

    public Pokemon3dApiIntegracaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
