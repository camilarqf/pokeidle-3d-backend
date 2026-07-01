package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StabCalculatorTest {

    private final StabCalculator calculator = new StabCalculator();

    @Test
    void deveRetornarStabParaAtacanteFireUsandoMoveFire() {
        assertStab(PokemonType.FIRE, List.of(PokemonType.FIRE));
    }

    @Test
    void deveRetornarStabParaAtacanteWaterUsandoMoveWater() {
        assertStab(PokemonType.WATER, List.of(PokemonType.WATER));
    }

    @Test
    void deveRetornarStabParaAtacanteGrassPoisonUsandoMovePoison() {
        assertStab(PokemonType.POISON, List.of(PokemonType.GRASS, PokemonType.POISON));
    }

    @Test
    void deveRetornarStabParaAtacanteGrassPoisonUsandoMoveGrass() {
        assertStab(PokemonType.GRASS, List.of(PokemonType.GRASS, PokemonType.POISON));
    }

    @Test
    void deveRetornarSemStabParaAtacanteFireUsandoMoveNormal() {
        assertNoStab(PokemonType.NORMAL, List.of(PokemonType.FIRE));
    }

    @Test
    void deveRetornarSemStabParaAtacanteWaterUsandoMoveElectric() {
        assertNoStab(PokemonType.ELECTRIC, List.of(PokemonType.WATER));
    }

    @Test
    void deveRetornarSemStabParaAtacanteGrassPoisonUsandoMoveFire() {
        assertNoStab(PokemonType.FIRE, List.of(PokemonType.GRASS, PokemonType.POISON));
    }

    @Test
    void deveLancarExcecaoQuandoMoveTypeForNulo() {
        assertThatThrownBy(() -> calculator.calculate(null, List.of(PokemonType.FIRE)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Tipo do movimento");
    }

    @Test
    void deveLancarExcecaoQuandoTiposDoAtacanteForemNulos() {
        assertThatThrownBy(() -> calculator.calculate(PokemonType.FIRE, null))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Tipos do atacante");
    }

    @Test
    void deveLancarExcecaoQuandoTiposDoAtacanteEstiveremVazios() {
        assertThatThrownBy(() -> calculator.calculate(PokemonType.FIRE, List.of()))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("ao menos um tipo do atacante");
    }

    @Test
    void deveLancarExcecaoQuandoHouverMaisDeDoisTiposDoAtacante() {
        assertThatThrownBy(() -> calculator.calculate(PokemonType.FIRE,
                List.of(PokemonType.FIRE, PokemonType.FLYING, PokemonType.DRAGON)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("maximo dois tipos");
    }

    @Test
    void deveLancarExcecaoQuandoHouverTipoDoAtacanteNulo() {
        assertThatThrownBy(() -> calculator.calculate(PokemonType.FIRE, Collections.singletonList(null)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Tipo do atacante");
    }

    @Test
    void deveLancarExcecaoQuandoHouverTiposDoAtacanteDuplicados() {
        assertThatThrownBy(() -> calculator.calculate(PokemonType.FIRE, List.of(PokemonType.FIRE, PokemonType.FIRE)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("duplicados");
    }

    private void assertStab(PokemonType moveType, List<PokemonType> attackerTypes) {
        assertThat(calculator.calculate(moveType, attackerTypes)).isEqualByComparingTo(StabCalculator.STAB);
    }

    private void assertNoStab(PokemonType moveType, List<PokemonType> attackerTypes) {
        assertThat(calculator.calculate(moveType, attackerTypes)).isEqualByComparingTo(StabCalculator.NO_STAB);
    }
}
