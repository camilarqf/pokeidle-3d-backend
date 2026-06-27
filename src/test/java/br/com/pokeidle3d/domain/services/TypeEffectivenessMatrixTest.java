package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TypeEffectivenessMatrixTest {

    private final TypeEffectivenessMatrix matrix = TypeEffectivenessMatrix.getInstance();

    @Test
    void deveRetornarMultiplicadorSuperEfetivo() {
        assertMultiplier(PokemonType.FIRE, PokemonType.GRASS, TypeEffectivenessMatrix.SUPER_EFFECTIVE);
        assertMultiplier(PokemonType.WATER, PokemonType.FIRE, TypeEffectivenessMatrix.SUPER_EFFECTIVE);
        assertMultiplier(PokemonType.ELECTRIC, PokemonType.WATER, TypeEffectivenessMatrix.SUPER_EFFECTIVE);
        assertMultiplier(PokemonType.ICE, PokemonType.DRAGON, TypeEffectivenessMatrix.SUPER_EFFECTIVE);
        assertMultiplier(PokemonType.FIGHTING, PokemonType.NORMAL, TypeEffectivenessMatrix.SUPER_EFFECTIVE);
    }

    @Test
    void deveRetornarMultiplicadorPoucoEfetivo() {
        assertMultiplier(PokemonType.FIRE, PokemonType.WATER, TypeEffectivenessMatrix.NOT_EFFECTIVE);
        assertMultiplier(PokemonType.WATER, PokemonType.GRASS, TypeEffectivenessMatrix.NOT_EFFECTIVE);
        assertMultiplier(PokemonType.ELECTRIC, PokemonType.GRASS, TypeEffectivenessMatrix.NOT_EFFECTIVE);
        assertMultiplier(PokemonType.BUG, PokemonType.FIRE, TypeEffectivenessMatrix.NOT_EFFECTIVE);
        assertMultiplier(PokemonType.NORMAL, PokemonType.ROCK, TypeEffectivenessMatrix.NOT_EFFECTIVE);
    }

    @Test
    void deveRetornarMultiplicadorImune() {
        assertMultiplier(PokemonType.NORMAL, PokemonType.GHOST, TypeEffectivenessMatrix.IMMUNE);
        assertMultiplier(PokemonType.GHOST, PokemonType.NORMAL, TypeEffectivenessMatrix.IMMUNE);
        assertMultiplier(PokemonType.ELECTRIC, PokemonType.GROUND, TypeEffectivenessMatrix.IMMUNE);
        assertMultiplier(PokemonType.GROUND, PokemonType.FLYING, TypeEffectivenessMatrix.IMMUNE);
        assertMultiplier(PokemonType.PSYCHIC, PokemonType.DARK, TypeEffectivenessMatrix.IMMUNE);
        assertMultiplier(PokemonType.DRAGON, PokemonType.FAIRY, TypeEffectivenessMatrix.IMMUNE);
        assertMultiplier(PokemonType.POISON, PokemonType.STEEL, TypeEffectivenessMatrix.IMMUNE);
    }

    @Test
    void deveRetornarMultiplicadorNeutroParaRelacoesNaoCadastradas() {
        assertMultiplier(PokemonType.FIRE, PokemonType.ELECTRIC, TypeEffectivenessMatrix.NORMAL);
        assertMultiplier(PokemonType.WATER, PokemonType.NORMAL, TypeEffectivenessMatrix.NORMAL);
        assertMultiplier(PokemonType.GRASS, PokemonType.ICE, TypeEffectivenessMatrix.NORMAL);
    }

    @Test
    void deveRetornarValorParaTodasAsCombinacoes() {
        int totalCombinacoes = 0;

        for (PokemonType attacker : PokemonType.values()) {
            for (PokemonType defender : PokemonType.values()) {
                BigDecimal multiplier = matrix.getMultiplier(attacker, defender);

                assertThat(multiplier).isNotNull();
                assertThat(multiplier)
                        .isIn(TypeEffectivenessMatrix.IMMUNE, TypeEffectivenessMatrix.NOT_EFFECTIVE,
                                TypeEffectivenessMatrix.NORMAL, TypeEffectivenessMatrix.SUPER_EFFECTIVE);

                totalCombinacoes++;
            }
        }

        assertThat(totalCombinacoes).isEqualTo(324);
    }

    @Test
    void deveLancarExcecaoQuandoTipoAtacanteForNulo() {
        assertThatThrownBy(() -> matrix.getMultiplier(null, PokemonType.NORMAL))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("attackingType");
    }

    @Test
    void deveLancarExcecaoQuandoTipoDefensorForNulo() {
        assertThatThrownBy(() -> matrix.getMultiplier(PokemonType.NORMAL, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("defendingType");
    }

    private void assertMultiplier(PokemonType attackingType, PokemonType defendingType, BigDecimal expectedMultiplier) {
        assertThat(matrix.getMultiplier(attackingType, defendingType)).isEqualByComparingTo(expectedMultiplier);
    }
}
