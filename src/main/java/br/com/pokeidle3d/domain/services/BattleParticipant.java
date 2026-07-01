package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;

import java.util.UUID;

public record BattleParticipant(
        UUID id,
        String name,
        int speed
) {

    public BattleParticipant {
        if (id == null) {
            throw new ValidacaoDominioException("Id do participante da batalha e obrigatorio");
        }

        if (speed < 0) {
            throw new ValidacaoDominioException("Speed do participante da batalha nao pode ser negativa");
        }
    }
}
