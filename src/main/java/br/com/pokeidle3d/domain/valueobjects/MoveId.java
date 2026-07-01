package br.com.pokeidle3d.domain.valueobjects;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;

public record MoveId(Long value) {

    public MoveId {
        if (value == null || value <= 0) {
            throw new DomainValidationException("MoveId deve ser positivo");
        }
    }
}
