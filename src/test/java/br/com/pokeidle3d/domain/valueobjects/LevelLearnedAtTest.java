package br.com.pokeidle3d.domain.valueobjects;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LevelLearnedAtTest {

    @Test
    void deveCriarLevelValido() {
        LevelLearnedAt level = new LevelLearnedAt(7);

        assertThat(level.value()).isEqualTo(7);
    }

    @Test
    void naoDeveCriarLevelMenorOuIgualAZero() {
        assertThatThrownBy(() -> new LevelLearnedAt(0))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("LevelLearnedAt");
    }
}
