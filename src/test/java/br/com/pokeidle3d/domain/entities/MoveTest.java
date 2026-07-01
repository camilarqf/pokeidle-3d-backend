package br.com.pokeidle3d.domain.entities;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;
import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoveTest {

    @Test
    void deveCriarMoveValido() {
        Move move = Move.criar("Tackle", PokemonType.NORMAL, 40, 100, MoveCategory.PHYSICAL, 35);

        assertThat(move.getName()).isEqualTo("tackle");
        assertThat(move.getType()).isEqualTo(PokemonType.NORMAL);
        assertThat(move.getCategory()).isEqualTo(MoveCategory.PHYSICAL);
    }

    @Test
    void devePermitirPowerNuloParaMoveDeStatus() {
        Move move = Move.criar("growl", PokemonType.NORMAL, null, 100, MoveCategory.STATUS, 40);

        assertThat(move.getPower()).isNull();
    }

    @Test
    void devePermitirAccuracyNulaParaMoveQueNuncaFalha() {
        Move move = Move.criar("swift", PokemonType.NORMAL, 60, null, MoveCategory.SPECIAL, 20);

        assertThat(move.getAccuracy()).isNull();
    }

    @Test
    void naoDeveCriarMoveComPpMenorOuIgualAZero() {
        assertThatThrownBy(() -> Move.criar("tackle", PokemonType.NORMAL, 40, 100, MoveCategory.PHYSICAL, 0))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("PP");
    }

    @Test
    void naoDeveCriarMoveComPowerNegativo() {
        assertThatThrownBy(() -> Move.criar("tackle", PokemonType.NORMAL, -1, 100, MoveCategory.PHYSICAL, 35))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Power");
    }

    @Test
    void naoDeveCriarMoveComAccuracyForaDoIntervalo() {
        assertThatThrownBy(() -> Move.criar("tackle", PokemonType.NORMAL, 40, 101, MoveCategory.PHYSICAL, 35))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Accuracy");
    }
}
