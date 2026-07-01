package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

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
    void deveCalcularEfetividadeContraPokemonComUmTipo() {
        assertEffectiveness(PokemonType.FIRE, List.of(PokemonType.GRASS), TypeEffectivenessMatrix.SUPER_EFFECTIVE);
        assertEffectiveness(PokemonType.FIRE, List.of(PokemonType.WATER), TypeEffectivenessMatrix.NOT_EFFECTIVE);
        assertEffectiveness(PokemonType.ELECTRIC, List.of(PokemonType.GROUND), TypeEffectivenessMatrix.IMMUNE);
        assertEffectiveness(PokemonType.NORMAL, List.of(PokemonType.NORMAL), TypeEffectivenessMatrix.NORMAL);
    }

    @Test
    void deveCalcularEfetividadeContraPokemonComDoisTipos() {
        assertEffectiveness(PokemonType.FIRE, List.of(PokemonType.GRASS, PokemonType.STEEL), BigDecimal.valueOf(4.0));
        assertEffectiveness(PokemonType.FIRE, List.of(PokemonType.WATER, PokemonType.ROCK), BigDecimal.valueOf(0.25));
        assertEffectiveness(PokemonType.ELECTRIC, List.of(PokemonType.WATER, PokemonType.FLYING), BigDecimal.valueOf(4.0));
        assertEffectiveness(PokemonType.ELECTRIC, List.of(PokemonType.WATER, PokemonType.GROUND), TypeEffectivenessMatrix.IMMUNE);
        assertEffectiveness(PokemonType.FIGHTING, List.of(PokemonType.NORMAL, PokemonType.DARK), BigDecimal.valueOf(4.0));
        assertEffectiveness(PokemonType.GROUND, List.of(PokemonType.FIRE, PokemonType.FLYING), TypeEffectivenessMatrix.IMMUNE);
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

    @Test
    void deveLancarExcecaoQuandoTipoAtacanteDaEfetividadeForNulo() {
        assertThatThrownBy(() -> matrix.getEffectiveness(null, List.of(PokemonType.NORMAL)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Tipo atacante");
    }

    @Test
    void deveLancarExcecaoQuandoTiposDefensoresForemNulos() {
        assertThatThrownBy(() -> matrix.getEffectiveness(PokemonType.NORMAL, null))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Tipos defensores");
    }

    @Test
    void deveLancarExcecaoQuandoTiposDefensoresEstiveremVazios() {
        assertThatThrownBy(() -> matrix.getEffectiveness(PokemonType.NORMAL, List.of()))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("ao menos um tipo defensor");
    }

    @Test
    void deveLancarExcecaoQuandoHouverMaisDeDoisTiposDefensores() {
        assertThatThrownBy(() -> matrix.getEffectiveness(PokemonType.NORMAL,
                List.of(PokemonType.NORMAL, PokemonType.FLYING, PokemonType.FAIRY)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("maximo dois tipos");
    }

    @Test
    void deveLancarExcecaoQuandoHouverTipoDefensorNulo() {
        assertThatThrownBy(() -> matrix.getEffectiveness(PokemonType.NORMAL,
                Collections.singletonList(null)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Tipo defensor");
    }

    @Test
    void deveLancarExcecaoQuandoHouverTiposDefensoresDuplicados() {
        assertThatThrownBy(() -> matrix.getEffectiveness(PokemonType.NORMAL,
                List.of(PokemonType.NORMAL, PokemonType.NORMAL)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("duplicados");
    }

    private void assertMultiplier(PokemonType attackingType, PokemonType defendingType, BigDecimal expectedMultiplier) {
        assertThat(matrix.getMultiplier(attackingType, defendingType)).isEqualByComparingTo(expectedMultiplier);
    }

    private void assertEffectiveness(
            PokemonType attackType,
            List<PokemonType> defenseTypes,
            BigDecimal expectedEffectiveness
    ) {
        assertThat(matrix.getEffectiveness(attackType, defenseTypes)).isEqualByComparingTo(expectedEffectiveness);
    }
}
