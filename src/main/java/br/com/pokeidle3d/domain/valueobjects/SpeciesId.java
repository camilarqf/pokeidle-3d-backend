package br.com.pokeidle3d.domain.valueobjects;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;

public record SpeciesId(Long value) {

    public SpeciesId {
        if (value == null || value <= 0) {
            throw new ValidacaoDominioException("SpeciesId deve ser positivo");
        }
    }
}
