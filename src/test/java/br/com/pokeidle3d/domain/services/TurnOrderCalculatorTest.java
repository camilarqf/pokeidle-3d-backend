package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TurnOrderCalculatorTest {

    @Test
    void deveRetornarAtacantePrimeiroQuandoSpeedForMaior() {
        TurnOrderCalculator calculator = new TurnOrderCalculator(new FixedTurnOrderRandomProvider(false));
        BattleParticipant attacker = participant("attacker", 100);
        BattleParticipant defender = participant("defender", 80);

        TurnOrder order = calculator.determine(attacker, defender);

        assertThat(order.first()).isEqualTo(attacker);
        assertThat(order.second()).isEqualTo(defender);
    }

    @Test
    void deveRetornarDefensorPrimeiroQuandoSpeedForMaior() {
        TurnOrderCalculator calculator = new TurnOrderCalculator(new FixedTurnOrderRandomProvider(true));
        BattleParticipant attacker = participant("attacker", 80);
        BattleParticipant defender = participant("defender", 120);

        TurnOrder order = calculator.determine(attacker, defender);

        assertThat(order.first()).isEqualTo(defender);
        assertThat(order.second()).isEqualTo(attacker);
    }

    @Test
    void deveRetornarPrimeiroParticipanteNoEmpateQuandoProviderRetornarTrue() {
        TurnOrderCalculator calculator = new TurnOrderCalculator(new FixedTurnOrderRandomProvider(true));
        BattleParticipant first = participant("first", 100);
        BattleParticipant second = participant("second", 100);

        TurnOrder order = calculator.determine(first, second);

        assertThat(order.first()).isEqualTo(first);
        assertThat(order.second()).isEqualTo(second);
    }

    @Test
    void deveRetornarSegundoParticipanteNoEmpateQuandoProviderRetornarFalse() {
        TurnOrderCalculator calculator = new TurnOrderCalculator(new FixedTurnOrderRandomProvider(false));
        BattleParticipant first = participant("first", 100);
        BattleParticipant second = participant("second", 100);

        TurnOrder order = calculator.determine(first, second);

        assertThat(order.first()).isEqualTo(second);
        assertThat(order.second()).isEqualTo(first);
    }

    @Test
    void deveLancarExcecaoQuandoPrimeiroParticipanteForNulo() {
        TurnOrderCalculator calculator = new TurnOrderCalculator(new FixedTurnOrderRandomProvider(true));

        assertThatThrownBy(() -> calculator.determine(null, participant("second", 100)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Primeiro participante");
    }

    @Test
    void deveLancarExcecaoQuandoSegundoParticipanteForNulo() {
        TurnOrderCalculator calculator = new TurnOrderCalculator(new FixedTurnOrderRandomProvider(true));

        assertThatThrownBy(() -> calculator.determine(participant("first", 100), null))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Segundo participante");
    }

    @Test
    void deveLancarExcecaoQuandoSpeedForNegativa() {
        assertThatThrownBy(() -> participant("invalid", -1))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Speed");
    }

    @Test
    void deveLancarExcecaoQuandoIdForNulo() {
        assertThatThrownBy(() -> new BattleParticipant(null, "invalid", 100))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Id do participante");
    }

    @Test
    void deveLancarExcecaoQuandoProviderAleatorioForNulo() {
        assertThatThrownBy(() -> new TurnOrderCalculator(null))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Provider aleatorio");
    }

    @Test
    void deveSerDeterministicoComProviderFixo() {
        TurnOrderCalculator calculator = new TurnOrderCalculator(new FixedTurnOrderRandomProvider(false));
        BattleParticipant first = participant("first", 100);
        BattleParticipant second = participant("second", 100);

        TurnOrder firstOrder = calculator.determine(first, second);
        TurnOrder secondOrder = calculator.determine(first, second);

        assertThat(firstOrder).isEqualTo(secondOrder);
        assertThat(secondOrder.first()).isEqualTo(second);
    }

    private BattleParticipant participant(String name, int speed) {
        return new BattleParticipant(UUID.randomUUID(), name, speed);
    }
}
