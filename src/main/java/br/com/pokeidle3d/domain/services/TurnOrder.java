package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;

public record TurnOrder(
        BattleParticipant first,
        BattleParticipant second
) {

    public TurnOrder {
        if (first == null) {
            throw new ValidacaoDominioException("Primeiro participante da ordem de turno e obrigatorio");
        }

        if (second == null) {
            throw new ValidacaoDominioException("Segundo participante da ordem de turno e obrigatorio");
        }
    }
}
