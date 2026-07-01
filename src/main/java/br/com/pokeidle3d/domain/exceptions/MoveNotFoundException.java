package br.com.pokeidle3d.domain.exceptions;

public class MoveNotFoundException extends RuntimeException {

    public MoveNotFoundException(String message) {
        super(message);
    }
}
