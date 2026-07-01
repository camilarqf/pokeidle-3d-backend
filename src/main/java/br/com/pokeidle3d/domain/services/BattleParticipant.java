package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;

import java.util.UUID;

public record BattleParticipant(
        UUID id,
        String name,
        int speed
) {

    public BattleParticipant {
        if (id == null) {
            throw new DomainValidationException("Id do participante da batalha e obrigatorio");
        }

        if (speed < 0) {
            throw new DomainValidationException("Speed do participante da batalha nao pode ser negativa");
        }
    }
}
