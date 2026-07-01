package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public final class DamageCalculator {

    private final StabCalculator stabCalculator = new StabCalculator();
    private final TypeEffectivenessMatrix typeEffectivenessMatrix = TypeEffectivenessMatrix.getInstance();
    private final DamageRandomFactorProvider randomFactorProvider;

    public DamageCalculator() {
        this(new RandomDamageRandomFactorProvider());
    }

    public DamageCalculator(DamageRandomFactorProvider randomFactorProvider) {
        if (randomFactorProvider == null) {
            throw new DomainValidationException("Provider de fator aleatorio de dano e obrigatorio");
        }

        this.randomFactorProvider = randomFactorProvider;
    }

    public int calculateDamage(DamageCalculationInput input) {
        int baseDamage = calculateBaseDamage(input);
        BigDecimal stabMultiplier = stabCalculator.calculate(input.moveType(), input.attackerTypes());
        BigDecimal typeEffectiveness = typeEffectivenessMatrix.getEffectiveness(input.moveType(), input.defenderTypes());

        if (typeEffectiveness.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }

        BigDecimal randomFactor = randomFactorProvider.nextFactor();
        BigDecimal damage = BigDecimal.valueOf(baseDamage)
                .multiply(stabMultiplier)
                .multiply(typeEffectiveness)
                .multiply(randomFactor)
                .setScale(0, RoundingMode.DOWN);

        return Math.max(1, toIntDamage(damage));
    }

    public int calculateBaseDamage(DamageCalculationInput input) {
        if (input == null) {
            throw new DomainValidationException("Dados do calculo de dano sao obrigatorios");
        }

        BigInteger damage = BigInteger.valueOf((2L * input.attackerLevel()) / 5 + 2)
                .multiply(BigInteger.valueOf(input.movePower()))
                .multiply(BigInteger.valueOf(input.attack()))
                .divide(BigInteger.valueOf(input.defense()))
                .divide(BigInteger.valueOf(50))
                .add(BigInteger.valueOf(2));

        return Math.max(1, toIntDamage(damage));
    }

    private int toIntDamage(BigDecimal damage) {
        if (damage.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new DomainValidationException("Dano calculado excede o limite inteiro suportado");
        }

        return damage.intValue();
    }

    private int toIntDamage(BigInteger damage) {
        if (damage.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new DomainValidationException("Dano calculado excede o limite inteiro suportado");
        }

        return damage.intValue();
    }
}
