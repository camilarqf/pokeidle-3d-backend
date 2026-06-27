package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DamageCalculatorTest {

    private final DamageCalculator calculator = new DamageCalculator();

    @Test
    void deveCalcularDanoBaseParaLevelBaixo() {
        DamageCalculationInput input = new DamageCalculationInput(5, 40, 49, 49);

        int damage = calculator.calculateBaseDamage(input);

        assertThat(damage).isEqualTo(5);
    }

    @Test
    void deveCalcularDanoBaseParaLevelIntermediario() {
        DamageCalculationInput input = new DamageCalculationInput(50, 90, 120, 100);

        int damage = calculator.calculateBaseDamage(input);

        assertThat(damage).isEqualTo(49);
    }

    @Test
    void deveCalcularDanoBaseParaLevelAlto() {
        DamageCalculationInput input = new DamageCalculationInput(100, 100, 200, 150);

        int damage = calculator.calculateBaseDamage(input);

        assertThat(damage).isEqualTo(114);
    }

    @Test
    void deveCalcularDanoBaseQuandoProdutoIntermediarioExcedeLimiteDoInt() {
        DamageCalculationInput input = new DamageCalculationInput(100, 50000, 50000, 50000);

        int damage = calculator.calculateBaseDamage(input);

        assertThat(damage).isEqualTo(42002);
    }

    @Test
    void deveRetornarNoMinimoUmDeDanoParaEntradasValidas() {
        DamageCalculationInput input = new DamageCalculationInput(1, 1, 1, Integer.MAX_VALUE);

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
        DamageCalculationInput input = new DamageCalculationInput(
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
    void deveLancarExcecaoQuandoLevelForMenorOuIgualAZero() {
        assertThatThrownBy(() -> new DamageCalculationInput(0, 40, 49, 49))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Nivel do atacante");
    }

    @Test
    void deveLancarExcecaoQuandoMovePowerForMenorOuIgualAZero() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 0, 49, 49))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Power do movimento");
    }

    @Test
    void deveLancarExcecaoQuandoAttackForMenorOuIgualAZero() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 0, 49))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Ataque");
    }

    @Test
    void deveLancarExcecaoQuandoDefenseForMenorOuIgualAZero() {
        assertThatThrownBy(() -> new DamageCalculationInput(5, 40, 49, 0))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("Defesa");
    }

    @Test
    void deveSerDeterministicoParaAMesmaEntrada() {
        DamageCalculationInput input = new DamageCalculationInput(50, 90, 120, 100);

        int firstDamage = calculator.calculateBaseDamage(input);
        int secondDamage = calculator.calculateBaseDamage(input);

        assertThat(secondDamage).isEqualTo(firstDamage);
    }
}
