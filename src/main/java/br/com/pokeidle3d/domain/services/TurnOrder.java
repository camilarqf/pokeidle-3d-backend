package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;

public record TurnOrder(
        BattleParticipant first,
        BattleParticipant second
) {

    public TurnOrder {
        if (first == null) {
            throw new DomainValidationException("Primeiro participante da ordem de turno e obrigatorio");
        }

        if (second == null) {
            throw new DomainValidationException("Segundo participante da ordem de turno e obrigatorio");
        }
    }
}
