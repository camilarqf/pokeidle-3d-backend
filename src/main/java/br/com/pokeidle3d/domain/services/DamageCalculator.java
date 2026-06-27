package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;

import java.math.BigInteger;

public final class DamageCalculator {

    public int calculateBaseDamage(DamageCalculationInput input) {
        if (input == null) {
            throw new ValidacaoDominioException("Dados do calculo de dano sao obrigatorios");
        }

        BigInteger damage = BigInteger.valueOf((2L * input.attackerLevel()) / 5 + 2)
                .multiply(BigInteger.valueOf(input.movePower()))
                .multiply(BigInteger.valueOf(input.attack()))
                .divide(BigInteger.valueOf(input.defense()))
                .divide(BigInteger.valueOf(50))
                .add(BigInteger.valueOf(2));

        return Math.max(1, toIntDamage(damage));
    }

    private int toIntDamage(BigInteger damage) {
        if (damage.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new ValidacaoDominioException("Dano calculado excede o limite inteiro suportado");
        }

        return damage.intValue();
    }
}
