package br.com.pokeidle3d.domain.exceptions;

public class DuplicateSpeciesMoveException extends RuntimeException {

    public DuplicateSpeciesMoveException(String message) {
        super(message);
    }
}
