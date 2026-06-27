package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.valueobjects.PokemonType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class TypeEffectivenessMatrix {

    public static final BigDecimal IMMUNE = BigDecimal.ZERO;
    public static final BigDecimal NOT_EFFECTIVE = BigDecimal.valueOf(0.5);
    public static final BigDecimal NORMAL = BigDecimal.ONE;
    public static final BigDecimal SUPER_EFFECTIVE = BigDecimal.valueOf(2.0);

    private static final TypeEffectivenessMatrix INSTANCE = new TypeEffectivenessMatrix();

    private final Map<PokemonType, Map<PokemonType, BigDecimal>> multipliers;

    private TypeEffectivenessMatrix() {
        this.multipliers = buildMatrix();
    }

    public static TypeEffectivenessMatrix getInstance() {
        return INSTANCE;
    }

    public BigDecimal getMultiplier(PokemonType attackingType, PokemonType defendingType) {
        Objects.requireNonNull(attackingType, "attackingType nao pode ser nulo");
        Objects.requireNonNull(defendingType, "defendingType nao pode ser nulo");

        return multipliers.get(attackingType).get(defendingType);
    }

    private static Map<PokemonType, Map<PokemonType, BigDecimal>> buildMatrix() {
        EnumMap<PokemonType, EnumMap<PokemonType, BigDecimal>> matrix = new EnumMap<>(PokemonType.class);

        for (PokemonType attackingType : PokemonType.values()) {
            EnumMap<PokemonType, BigDecimal> row = new EnumMap<>(PokemonType.class);

            for (PokemonType defendingType : PokemonType.values()) {
                row.put(defendingType, NORMAL);
            }

            matrix.put(attackingType, row);
        }

        register(matrix, PokemonType.NORMAL, NOT_EFFECTIVE, PokemonType.ROCK, PokemonType.STEEL);
        register(matrix, PokemonType.NORMAL, IMMUNE, PokemonType.GHOST);

        register(matrix, PokemonType.FIRE, SUPER_EFFECTIVE, PokemonType.GRASS, PokemonType.ICE, PokemonType.BUG, PokemonType.STEEL);
        register(matrix, PokemonType.FIRE, NOT_EFFECTIVE, PokemonType.FIRE, PokemonType.WATER, PokemonType.ROCK, PokemonType.DRAGON);

        register(matrix, PokemonType.WATER, SUPER_EFFECTIVE, PokemonType.FIRE, PokemonType.GROUND, PokemonType.ROCK);
        register(matrix, PokemonType.WATER, NOT_EFFECTIVE, PokemonType.WATER, PokemonType.GRASS, PokemonType.DRAGON);

        register(matrix, PokemonType.ELECTRIC, SUPER_EFFECTIVE, PokemonType.WATER, PokemonType.FLYING);
        register(matrix, PokemonType.ELECTRIC, NOT_EFFECTIVE, PokemonType.ELECTRIC, PokemonType.GRASS, PokemonType.DRAGON);
        register(matrix, PokemonType.ELECTRIC, IMMUNE, PokemonType.GROUND);

        register(matrix, PokemonType.GRASS, SUPER_EFFECTIVE, PokemonType.WATER, PokemonType.GROUND, PokemonType.ROCK);
        register(matrix, PokemonType.GRASS, NOT_EFFECTIVE, PokemonType.FIRE, PokemonType.GRASS, PokemonType.POISON,
                PokemonType.FLYING, PokemonType.BUG, PokemonType.DRAGON, PokemonType.STEEL);

        register(matrix, PokemonType.ICE, SUPER_EFFECTIVE, PokemonType.GRASS, PokemonType.GROUND, PokemonType.FLYING,
                PokemonType.DRAGON);
        register(matrix, PokemonType.ICE, NOT_EFFECTIVE, PokemonType.FIRE, PokemonType.WATER, PokemonType.ICE,
                PokemonType.STEEL);

        register(matrix, PokemonType.FIGHTING, SUPER_EFFECTIVE, PokemonType.NORMAL, PokemonType.ICE, PokemonType.ROCK,
                PokemonType.DARK, PokemonType.STEEL);
        register(matrix, PokemonType.FIGHTING, NOT_EFFECTIVE, PokemonType.POISON, PokemonType.FLYING,
                PokemonType.PSYCHIC, PokemonType.BUG, PokemonType.FAIRY);
        register(matrix, PokemonType.FIGHTING, IMMUNE, PokemonType.GHOST);

        register(matrix, PokemonType.POISON, SUPER_EFFECTIVE, PokemonType.GRASS, PokemonType.FAIRY);
        register(matrix, PokemonType.POISON, NOT_EFFECTIVE, PokemonType.POISON, PokemonType.GROUND, PokemonType.ROCK,
                PokemonType.GHOST);
        register(matrix, PokemonType.POISON, IMMUNE, PokemonType.STEEL);

        register(matrix, PokemonType.GROUND, SUPER_EFFECTIVE, PokemonType.FIRE, PokemonType.ELECTRIC,
                PokemonType.POISON, PokemonType.ROCK, PokemonType.STEEL);
        register(matrix, PokemonType.GROUND, NOT_EFFECTIVE, PokemonType.GRASS, PokemonType.BUG);
        register(matrix, PokemonType.GROUND, IMMUNE, PokemonType.FLYING);

        register(matrix, PokemonType.FLYING, SUPER_EFFECTIVE, PokemonType.GRASS, PokemonType.FIGHTING, PokemonType.BUG);
        register(matrix, PokemonType.FLYING, NOT_EFFECTIVE, PokemonType.ELECTRIC, PokemonType.ROCK, PokemonType.STEEL);

        register(matrix, PokemonType.PSYCHIC, SUPER_EFFECTIVE, PokemonType.FIGHTING, PokemonType.POISON);
        register(matrix, PokemonType.PSYCHIC, NOT_EFFECTIVE, PokemonType.PSYCHIC, PokemonType.STEEL);
        register(matrix, PokemonType.PSYCHIC, IMMUNE, PokemonType.DARK);

        register(matrix, PokemonType.BUG, SUPER_EFFECTIVE, PokemonType.GRASS, PokemonType.PSYCHIC, PokemonType.DARK);
        register(matrix, PokemonType.BUG, NOT_EFFECTIVE, PokemonType.FIRE, PokemonType.FIGHTING, PokemonType.POISON,
                PokemonType.FLYING, PokemonType.GHOST, PokemonType.STEEL, PokemonType.FAIRY);

        register(matrix, PokemonType.ROCK, SUPER_EFFECTIVE, PokemonType.FIRE, PokemonType.ICE, PokemonType.FLYING,
                PokemonType.BUG);
        register(matrix, PokemonType.ROCK, NOT_EFFECTIVE, PokemonType.FIGHTING, PokemonType.GROUND, PokemonType.STEEL);

        register(matrix, PokemonType.GHOST, SUPER_EFFECTIVE, PokemonType.PSYCHIC, PokemonType.GHOST);
        register(matrix, PokemonType.GHOST, NOT_EFFECTIVE, PokemonType.DARK);
        register(matrix, PokemonType.GHOST, IMMUNE, PokemonType.NORMAL);

        register(matrix, PokemonType.DRAGON, SUPER_EFFECTIVE, PokemonType.DRAGON);
        register(matrix, PokemonType.DRAGON, NOT_EFFECTIVE, PokemonType.STEEL);
        register(matrix, PokemonType.DRAGON, IMMUNE, PokemonType.FAIRY);

        register(matrix, PokemonType.DARK, SUPER_EFFECTIVE, PokemonType.PSYCHIC, PokemonType.GHOST);
        register(matrix, PokemonType.DARK, NOT_EFFECTIVE, PokemonType.FIGHTING, PokemonType.DARK, PokemonType.FAIRY);

        register(matrix, PokemonType.STEEL, SUPER_EFFECTIVE, PokemonType.ICE, PokemonType.ROCK, PokemonType.FAIRY);
        register(matrix, PokemonType.STEEL, NOT_EFFECTIVE, PokemonType.FIRE, PokemonType.WATER, PokemonType.ELECTRIC,
                PokemonType.STEEL);

        register(matrix, PokemonType.FAIRY, SUPER_EFFECTIVE, PokemonType.FIGHTING, PokemonType.DRAGON, PokemonType.DARK);
        register(matrix, PokemonType.FAIRY, NOT_EFFECTIVE, PokemonType.FIRE, PokemonType.POISON, PokemonType.STEEL);

        return freeze(matrix);
    }

    private static void register(
            EnumMap<PokemonType, EnumMap<PokemonType, BigDecimal>> matrix,
            PokemonType attackingType,
            BigDecimal multiplier,
            PokemonType... defendingTypes
    ) {
        EnumMap<PokemonType, BigDecimal> row = matrix.get(attackingType);

        for (PokemonType defendingType : defendingTypes) {
            row.put(defendingType, multiplier);
        }
    }

    private static Map<PokemonType, Map<PokemonType, BigDecimal>> freeze(
            EnumMap<PokemonType, EnumMap<PokemonType, BigDecimal>> matrix
    ) {
        EnumMap<PokemonType, Map<PokemonType, BigDecimal>> immutableMatrix = new EnumMap<>(PokemonType.class);

        for (Map.Entry<PokemonType, EnumMap<PokemonType, BigDecimal>> entry : matrix.entrySet()) {
            immutableMatrix.put(entry.getKey(), Collections.unmodifiableMap(new EnumMap<>(entry.getValue())));
        }

        return Collections.unmodifiableMap(immutableMatrix);
    }
}
