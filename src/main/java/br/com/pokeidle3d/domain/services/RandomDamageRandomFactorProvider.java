package br.com.pokeidle3d.domain.services;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomDamageRandomFactorProvider implements DamageRandomFactorProvider {

    private static final int MIN_PERCENT = 85;
    private static final int MAX_PERCENT = 100;

    @Override
    public BigDecimal nextFactor() {
        int percent = ThreadLocalRandom.current().nextInt(MIN_PERCENT, MAX_PERCENT + 1);

        return BigDecimal.valueOf(percent, 2);
    }
}
