package br.com.pokeidle3d.domain.services;

import java.math.BigDecimal;

public interface DamageRandomFactorProvider {

    BigDecimal MIN_FACTOR = BigDecimal.valueOf(0.85);
    BigDecimal MAX_FACTOR = BigDecimal.valueOf(1.00);

    BigDecimal nextFactor();
}
