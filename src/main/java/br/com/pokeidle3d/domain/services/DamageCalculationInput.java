package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;

import java.util.EnumSet;
import java.util.List;

public record DamageCalculationInput(
        int attackerLevel,
        int movePower,
        int attack,
        int defense,
        PokemonType moveType,
        List<PokemonType> attackerTypes,
        List<PokemonType> defenderTypes
) {

    public DamageCalculationInput {
        validatePositive(attackerLevel, "Nivel do atacante");
        validatePositive(movePower, "Power do movimento");
        validatePositive(attack, "Ataque");
        validatePositive(defense, "Defesa");
        validateMoveType(moveType);
        validatePokemonTypes(
                attackerTypes,
                "Tipos do atacante sao obrigatorios",
                "Deve haver ao menos um tipo do atacante",
                "Pokemon atacante deve ter no maximo dois tipos",
                "Tipo do atacante nao pode ser nulo",
                "Tipos do atacante nao podem ser duplicados"
        );
        validatePokemonTypes(
                defenderTypes,
                "Tipos defensores sao obrigatorios",
                "Deve haver ao menos um tipo defensor",
                "Pokemon defensor deve ter no maximo dois tipos",
                "Tipo defensor nao pode ser nulo",
                "Tipos defensores nao podem ser duplicados"
        );
        attackerTypes = List.copyOf(attackerTypes);
        defenderTypes = List.copyOf(defenderTypes);
    }

    private static void validatePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidacaoDominioException(fieldName + " deve ser maior que zero");
        }
    }

    private static void validateMoveType(PokemonType moveType) {
        if (moveType == null) {
            throw new ValidacaoDominioException("Tipo do movimento e obrigatorio");
        }
    }

    private static void validatePokemonTypes(
            List<PokemonType> types,
            String nullMessage,
            String emptyMessage,
            String tooManyMessage,
            String nullTypeMessage,
            String duplicatedMessage
    ) {
        if (types == null) {
            throw new ValidacaoDominioException(nullMessage);
        }

        if (types.isEmpty()) {
            throw new ValidacaoDominioException(emptyMessage);
        }

        if (types.size() > 2) {
            throw new ValidacaoDominioException(tooManyMessage);
        }

        EnumSet<PokemonType> uniqueTypes = EnumSet.noneOf(PokemonType.class);

        for (PokemonType type : types) {
            if (type == null) {
                throw new ValidacaoDominioException(nullTypeMessage);
            }

            if (!uniqueTypes.add(type)) {
                throw new ValidacaoDominioException(duplicatedMessage);
            }
        }
    }
}
