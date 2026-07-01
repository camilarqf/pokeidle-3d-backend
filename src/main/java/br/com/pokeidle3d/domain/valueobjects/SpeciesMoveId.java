package br.com.pokeidle3d.domain.valueobjects;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;

public record SpeciesMoveId(Long value) {

    public SpeciesMoveId {
        if (value == null || value <= 0) {
            throw new DomainValidationException("Id da associacao SpeciesMove deve ser positivo");
        }
    }
}
