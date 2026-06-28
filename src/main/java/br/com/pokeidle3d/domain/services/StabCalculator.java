package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

public final class StabCalculator {

    public static final BigDecimal STAB = BigDecimal.valueOf(1.5);
    public static final BigDecimal NO_STAB = BigDecimal.ONE;

    public BigDecimal calculate(PokemonType moveType, List<PokemonType> attackerTypes) {
        validate(moveType, attackerTypes);

        if (attackerTypes.contains(moveType)) {
            return STAB;
        }

        return NO_STAB;
    }

    private static void validate(PokemonType moveType, List<PokemonType> attackerTypes) {
        if (moveType == null) {
            throw new ValidacaoDominioException("Tipo do movimento e obrigatorio");
        }

        if (attackerTypes == null) {
            throw new ValidacaoDominioException("Tipos do atacante sao obrigatorios");
        }

        if (attackerTypes.isEmpty()) {
            throw new ValidacaoDominioException("Deve haver ao menos um tipo do atacante");
        }

        if (attackerTypes.size() > 2) {
            throw new ValidacaoDominioException("Pokemon atacante deve ter no maximo dois tipos");
        }

        EnumSet<PokemonType> uniqueAttackerTypes = EnumSet.noneOf(PokemonType.class);

        for (PokemonType attackerType : attackerTypes) {
            if (attackerType == null) {
                throw new ValidacaoDominioException("Tipo do atacante nao pode ser nulo");
            }

            if (!uniqueAttackerTypes.add(attackerType)) {
                throw new ValidacaoDominioException("Tipos do atacante nao podem ser duplicados");
            }
        }
    }
}
