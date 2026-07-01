package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;

import java.math.BigDecimal;

public final class FixedDamageRandomFactorProvider implements DamageRandomFactorProvider {

    private final BigDecimal factor;

    public FixedDamageRandomFactorProvider(BigDecimal factor) {
        if (factor == null) {
            throw new DomainValidationException("Fator aleatorio fixo de dano e obrigatorio");
        }

        if (factor.compareTo(MIN_FACTOR) < 0) {
            throw new DomainValidationException("Fator aleatorio fixo de dano nao pode ser menor que 0.85");
        }

        if (factor.compareTo(MAX_FACTOR) > 0) {
            throw new DomainValidationException("Fator aleatorio fixo de dano nao pode ser maior que 1.00");
        }

        this.factor = factor;
    }

    @Override
    public BigDecimal nextFactor() {
        return factor;
    }
}
