package br.com.pokeidle3d.domain.valueobjects;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;

public record SpeciesId(Long value) {

    public SpeciesId {
        if (value == null || value <= 0) {
            throw new DomainValidationException("SpeciesId deve ser positivo");
        }
    }
}
