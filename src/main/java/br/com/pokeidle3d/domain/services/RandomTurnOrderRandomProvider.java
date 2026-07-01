package br.com.pokeidle3d.domain.services;

import java.util.concurrent.ThreadLocalRandom;

public final class RandomTurnOrderRandomProvider implements TurnOrderRandomProvider {

    @Override
    public boolean firstActsFirst() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
