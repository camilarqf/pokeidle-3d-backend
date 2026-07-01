package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;

public final class TurnOrderCalculator {

    private final TurnOrderRandomProvider randomProvider;

    public TurnOrderCalculator() {
        this(new RandomTurnOrderRandomProvider());
    }

    public TurnOrderCalculator(TurnOrderRandomProvider randomProvider) {
        if (randomProvider == null) {
            throw new DomainValidationException("Provider aleatorio de ordem de turno e obrigatorio");
        }

        this.randomProvider = randomProvider;
    }

    public TurnOrder determine(BattleParticipant first, BattleParticipant second) {
        if (first == null) {
            throw new DomainValidationException("Primeiro participante da batalha e obrigatorio");
        }

        if (second == null) {
            throw new DomainValidationException("Segundo participante da batalha e obrigatorio");
        }

        if (first.speed() > second.speed()) {
            return new TurnOrder(first, second);
        }

        if (second.speed() > first.speed()) {
            return new TurnOrder(second, first);
        }

        if (randomProvider.firstActsFirst()) {
            return new TurnOrder(first, second);
        }

        return new TurnOrder(second, first);
    }
}
