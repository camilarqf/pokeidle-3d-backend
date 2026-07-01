package br.com.pokeidle3d.application.exceptions;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(String message) {
        super(message);
    }
}
