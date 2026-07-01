package br.com.pokeidle3d.domain.exceptions;

public class SpeciesNotFoundException extends RuntimeException {

    public SpeciesNotFoundException(String message) {
        super(message);
    }
}
