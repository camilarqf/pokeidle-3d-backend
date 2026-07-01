package br.com.pokeidle3d.domain.exceptions;

public class DuplicateSpeciesException extends RuntimeException {

    public DuplicateSpeciesException(String message) {
        super(message);
    }
}
