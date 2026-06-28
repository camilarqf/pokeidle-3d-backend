package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DamageCalculatorTest {

    private final DamageCalculator calculator = new DamageCalculator();

    @Test
    void deveCalcularDanoBaseParaLevelBaixo() {
        DamageCalculationInput input = input(5, 40, 49, 49);

        int damage = calculator.calculateBaseDamage(input);

        assertThat(damage).isEqualTo(5);
    }

    @Test
    void deveCalcularDanoBaseParaLevelIntermediario() {
        DamageCalculationInput input = input(50, 90, 120, 100);

        int damage = calculator.calculateBaseDamage(input);

        assertThat(damage).isEqualTo(49);
    }

    @Test
    void deveCalcularDanoBaseParaLevelAlto() {
        DamageCalculationInput input = input(100, 100, 200, 150);

        int damage = calculator.calculateBaseDamage(input);

        assertThat(damage).isEqualTo(114);
    }

    @Test
    void deveCalcularDanoBaseQuandoProdutoIntermediarioExcedeLimiteDoInt() {
        DamageCalculationInput input = input(100, 50000, 50000, 50000);

        int damage = calculator.calculateBaseDamage(input);

        assertThat(damage).isEqualTo(42002);
    }

    @Test
    void deveRetornarNoMinimoUmDeDanoParaEntradasValidas() {
        DamageCalculationInput input = input(1, 1, 1, Integer.MAX_VALUE);

        int damage = calculator.calculateBaseDamage(input);

        assertThat(damage).isGreaterThanOrEqualTo(1);
    }

    @Test
    void deveLancarExcecaoQuandoInputForNulo() {
        assertThatThrownBy(() -> calculator.calculateBaseDamage(null))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("calculo de dano");
    }

    @Test
    void deveLancarExcecaoQuandoDanoFinalExcederLimiteDoInt() {
        DamageCalculationInput input = input(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                1
        );

        assertThatThrownBy(() -> calculator.calculateBaseDamage(input))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("limite inteiro");
    }

    @Test
    void deveAplicarEfetividadeSimples() {
        assertThat(damage(PokemonType.FIRE, List.of(PokemonType.NORMAL), List.of(PokemonType.GRASS))).isEqualTo(20);
        assertThat(damage(PokemonType.FIRE, List.of(PokemonType.NORMAL), List.of(PokemonType.WATER))).isEqualTo(5);
        assertThat(damage(PokemonType.ELECTRIC, List.of(PokemonType.NORMAL), List.of(PokemonType.GROUND))).isZero();
        assertThat(damage(PokemonType.NORMAL, List.of(PokemonType.FIRE), List.of(PokemonType.GHOST))).isZero();
        assertThat(damage(PokemonType.NORMAL, List.of(PokemonType.FIRE), List.of(PokemonType.NORMAL))).isEqualTo(10);
    }

    @Test
    void deveAplicarEfetividadeComDoisTiposDefensores() {
        assertThat(damage(PokemonType.FIRE, List.of(PokemonType.NORMAL), List.of(PokemonType.GRASS, PokemonType.STEEL)))
                .isEqualTo(40);
        assertThat(damage(PokemonType.FIRE, List.of(PokemonType.NORMAL), List.of(PokemonType.WATER, PokemonType.ROCK)))
                .isEqualTo(2);
        assertThat(damage(PokemonType.ELECTRIC, List.of(PokemonType.NORMAL), List.of(PokemonType.WATER, PokemonType.FLYING)))
                .isEqualTo(40);
        assertThat(damage(PokemonType.ELECTRIC, List.of(PokemonType.NORMAL), List.of(PokemonType.WATER, PokemonType.GROUND)))
                .isZero();
        assertThat(damage(PokemonType.FIGHTING, List.of(PokemonType.NORMAL), List.of(PokemonType.NORMAL, PokemonType.DARK)))
                .isEqualTo(40);
        assertThat(damage(PokemonType.GROUND, List.of(PokemonType.NORMAL), List.of(PokemonType.FIRE, PokemonType.FLYING)))
                .isZero();
    }

    @Test
    void deveCombinarStabComEfetividade() {
        assertThat(damage(PokemonType.FIRE, List.of(PokemonType.FIRE), List.of(PokemonType.GRASS))).isEqualTo(30);
        assertThat(damage(PokemonType.WATER, List.of(PokemonType.WATER), List.of(PokemonType.FIRE))).isEqualTo(30);
        assertThat(damage(PokemonType.POISON, List.of(PokemonType.GRASS, PokemonType.POISON), List.of(PokemonType.FAIRY)))
                .isEqualTo(30);
    }

    @Test
    void deveRetornarZeroQuandoEfetividadeForImunidade() {
        DamageCalculationInput input = new DamageCalculationInput(
                20,
                40,
                50,
                50,
                PokemonType.NORMAL,
                List.of(PokemonType.NORMAL),
                List.of(PokemonType.GHOST)
        );

        int damage = calculator.calculateDamage(input);

        assertThat(damage).isZero();
    }

    @Test
    void naoDeveAplicarDanoMinimoEmCasoDeImunidade() {
        DamageCalculationInput input = new DamageCalculationInput(
                1,
                1,
                1,
                Integer.MAX_VALUE,
                PokemonType.ELECTRIC,
                List.of(PokemonType.ELECTRIC),
                List.of(PokemonType.GROUND)
        );

        int damage = calculator.calculateDamage(input);

        assertThat(damage).isZero();
    }

    @Test
    void deveArredondarDanoFinalParaBaixo() {
        DamageCalculationInput input = new DamageCalculationInput(
                13,
                40,
                50,
                50,
                PokemonType.FIRE,
                List.of(PokemonType.FIRE),
                List.of(PokemonType.WATER)
        );

        int damage = calculator.calculateDamage(input);

        assertThat(damage).isEqualTo(5);
    }

    @Test
    void deveAplicarDanoMinimoQuandoMultiplicadorFinalForMaiorQueZero() {
        DamageCalculationInput input = new DamageCalculationInput(
                1,
                1,
                1,
                Integer.MAX_VALUE,
                PokemonType.FIRE,
                List.of(PokemonType.NORMAL),
                List.of(PokemonType.WATER, PokemonType.ROCK)
        );

        int damage = calculator.calculateDamage(input);

        assertThat(damage).isEqualTo(1);
    }

    @Test
    void deveLancarExcecaoQuandoLevelForMenorOuIgualAZero() {
        assertThatThrownBy(() -> input(0, 40, 49, 49))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Nivel do atacante");
    }

    @Test
    void deveLancarExcecaoQuandoMovePowerForMenorOuIgualAZero() {
        assertThatThrownBy(() -> input(5, 0, 49, 49))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Power do movimento");
    }

    @Test
    void deveLancarExcecaoQuandoAttackForMenorOuIgualAZero() {
        assertThatThrownBy(() -> input(5, 40, 0, 49))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Ataque");
    }

    @Test
    void deveLancarExcecaoQuandoDefenseForMenorOuIgualAZero() {
        assertThatThrownBy(() -> input(5, 40, 49, 0))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Defesa");
    }

    @Test
    void deveLancarExcecaoQuandoMoveTypeForNulo() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, null,
                List.of(PokemonType.FIRE), List.of(PokemonType.NORMAL)))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Tipo do movimento");
    }

    @Test
    void deveLancarExcecaoQuandoTiposDoAtacanteForemNulos() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, PokemonType.FIRE,
                null, List.of(PokemonType.NORMAL)))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Tipos do atacante");
    }

    @Test
    void deveLancarExcecaoQuandoTiposDoAtacanteEstiveremVazios() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, PokemonType.FIRE,
                List.of(), List.of(PokemonType.NORMAL)))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("ao menos um tipo do atacante");
    }

    @Test
    void deveLancarExcecaoQuandoHouverMaisDeDoisTiposDoAtacante() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, PokemonType.FIRE,
                List.of(PokemonType.FIRE, PokemonType.FLYING, PokemonType.DRAGON), List.of(PokemonType.NORMAL)))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("maximo dois tipos");
    }

    @Test
    void deveLancarExcecaoQuandoHouverTipoDoAtacanteNulo() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, PokemonType.FIRE,
                Collections.singletonList(null), List.of(PokemonType.NORMAL)))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Tipo do atacante");
    }

    @Test
    void deveLancarExcecaoQuandoHouverTiposDoAtacanteDuplicados() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, PokemonType.FIRE,
                List.of(PokemonType.FIRE, PokemonType.FIRE), List.of(PokemonType.NORMAL)))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("duplicados");
    }

    @Test
    void deveLancarExcecaoQuandoTiposDoDefensorForemNulos() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, PokemonType.FIRE,
                List.of(PokemonType.FIRE), null))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Tipos defensores");
    }

    @Test
    void deveLancarExcecaoQuandoTiposDoDefensorEstiveremVazios() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, PokemonType.FIRE,
                List.of(PokemonType.FIRE), List.of()))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("ao menos um tipo defensor");
    }

    @Test
    void deveLancarExcecaoQuandoHouverMaisDeDoisTiposDoDefensor() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, PokemonType.FIRE,
                List.of(PokemonType.FIRE), List.of(PokemonType.FIRE, PokemonType.FLYING, PokemonType.DRAGON)))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("maximo dois tipos");
    }

    @Test
    void deveLancarExcecaoQuandoHouverTipoDoDefensorNulo() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, PokemonType.FIRE,
                List.of(PokemonType.FIRE), Collections.singletonList(null)))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Tipo defensor");
    }

    @Test
    void deveLancarExcecaoQuandoHouverTiposDoDefensorDuplicados() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 49, PokemonType.FIRE,
                List.of(PokemonType.FIRE), List.of(PokemonType.FIRE, PokemonType.FIRE)))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("duplicados");
    }

    @Test
    void deveSerDeterministicoParaAMesmaEntrada() {
        DamageCalculationInput input = input(50, 90, 120, 100);

        int firstDamage = calculator.calculateDamage(input);
        int secondDamage = calculator.calculateDamage(input);

        assertThat(secondDamage).isEqualTo(firstDamage);
    }

    private int damage(PokemonType moveType, List<PokemonType> attackerTypes, List<PokemonType> defenderTypes) {
        DamageCalculationInput input = new DamageCalculationInput(
                20,
                40,
                50,
                50,
                moveType,
                attackerTypes,
                defenderTypes
        );

        return calculator.calculateDamage(input);
    }

    private DamageCalculationInput input(int attackerLevel, int movePower, int attack, int defense) {
        return new DamageCalculationInput(
                attackerLevel,
                movePower,
                attack,
                defense,
                PokemonType.NORMAL,
                List.of(PokemonType.NORMAL),
                List.of(PokemonType.NORMAL)
        );
    }
}
