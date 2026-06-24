package br.com.pokeidle3d.domain.valueobjects;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;

public record SpeciesMoveId(Long value) {

    public SpeciesMoveId {
        if (value == null || value <= 0) {
            throw new ValidacaoDominioException("Id da associacao SpeciesMove deve ser positivo");
        }
    }
}
