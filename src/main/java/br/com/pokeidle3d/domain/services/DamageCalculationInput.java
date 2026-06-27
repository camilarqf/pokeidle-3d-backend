package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;

public record DamageCalculationInput(
        int attackerLevel,
        int movePower,
        int attack,
        int defense
) {

    public DamageCalculationInput {
        validatePositive(attackerLevel, "Nivel do atacante");
        validatePositive(movePower, "Power do movimento");
        validatePositive(attack, "Ataque");
        validatePositive(defense, "Defesa");
    }

    private static void validatePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidacaoDominioException(fieldName + " deve ser maior que zero");
        }
    }
}
