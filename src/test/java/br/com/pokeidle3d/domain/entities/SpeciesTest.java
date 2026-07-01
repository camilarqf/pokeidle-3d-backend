package br.com.pokeidle3d.domain.entities;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;
import br.com.pokeidle3d.domain.events.SpeciesCreatedEvent;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
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
                .isInstanceOf(DomainValidationException.class)
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

    @Test
    void deveRegistrarEventoAoCriarSpecies() {
        CorrelationKey correlationKey = new CorrelationKey("species-test-key");

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
                "modelo",
                correlationKey
        );

        assertThat(species.eventosDominio()).hasSize(1);
        assertThat(species.eventosDominio().get(0)).isInstanceOf(SpeciesCreatedEvent.class);

        SpeciesCreatedEvent event = (SpeciesCreatedEvent) species.eventosDominio().get(0);
        assertThat(event.eventId()).isNotNull();
        assertThat(event.occurredAt()).isNotNull();
        assertThat(event.correlationKey()).isEqualTo(correlationKey);
        assertThat(event.pokedexNumber()).isEqualTo(1);
        assertThat(event.name()).isEqualTo("bulbasaur");
    }
}
