package br.com.pokeidle3d.domain.entities;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpeciesTest {

    @Test
    void deveCriarSpeciesValida() {
        Species species = Species.criar(
                1,
                "Bulbasaur",
                PokemonType.GRASS,
                PokemonType.POISON,
                45,
                49,
                49,
                65,
                65,
                45,
                "sprite",
                "modelo"
        );

        assertThat(species.getPokedexNumber()).isEqualTo(1);
        assertThat(species.getName()).isEqualTo("bulbasaur");
        assertThat(species.getSecondaryType()).isEqualTo(PokemonType.POISON);
    }

    @Test
    void naoDeveCriarSpeciesComStatNegativo() {
        assertThatThrownBy(() -> Species.criar(
                1,
                "bulbasaur",
                PokemonType.GRASS,
                PokemonType.POISON,
                -1,
                49,
                49,
                65,
                65,
                45,
                "sprite",
                "modelo"
        ))
                .isInstanceOf(ValidacaoDominioException.class)
                .hasMessageContaining("baseHp");
    }

    @Test
    void devePermitirTipoSecundarioOpcional() {
        Species species = Species.criar(
                4,
                "charmander",
                PokemonType.FIRE,
                null,
                39,
                52,
                43,
                60,
                50,
                65,
                "sprite",
                "modelo"
        );

        assertThat(species.getSecondaryType()).isNull();
    }
}
