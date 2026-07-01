package br.com.pokeidle3d.domain.exceptions;

public class DuplicateMoveException extends RuntimeException {

    public DuplicateMoveException(String message) {
        super(message);
    }
}
