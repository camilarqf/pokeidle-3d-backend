package br.com.pokeidle3d.infra.integrations.pokeapi;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PokeApiSpeciesMapperTest {

    private final PokeApiSpeciesMapper mapper = new PokeApiSpeciesMapper();

    @Test
    void deveMapearPokemonComDoisTiposEStatsBase() {
        Species species = mapper.paraSpecies(responseBulbasaur());

        assertThat(species.getPokedexNumber()).isEqualTo(1);
        assertThat(species.getName()).isEqualTo("bulbasaur");
        assertThat(species.getPrimaryType()).isEqualTo(PokemonType.GRASS);
        assertThat(species.getSecondaryType()).isEqualTo(PokemonType.POISON);
        assertThat(species.getBaseHp()).isEqualTo(45);
        assertThat(species.getBaseAttack()).isEqualTo(49);
        assertThat(species.getBaseDefense()).isEqualTo(49);
        assertThat(species.getBaseSpecialAttack()).isEqualTo(65);
        assertThat(species.getBaseSpecialDefense()).isEqualTo(65);
        assertThat(species.getBaseSpeed()).isEqualTo(45);
        assertThat(species.getSpriteRef()).isEqualTo("https://img/bulbasaur.png");
        assertThat(species.getModel3dRef())
                .isEqualTo("https://raw.githubusercontent.com/Pokemon-3D-api/assets/main/models/opt/regular/1.glb");
        assertThat(species.events()).isEmpty();
    }

    @Test
    void deveMapearPokemonComUmTipoESpriteAusente() {
        Species species = mapper.paraSpecies(new PokeApiPokemonResponse(
                4,
                "charmander",
                List.of(tipo(1, "fire")),
                stats(39, 52, 43, 60, 50, 65),
                null
        ));

        assertThat(species.getPrimaryType()).isEqualTo(PokemonType.FIRE);
        assertThat(species.getSecondaryType()).isNull();
        assertThat(species.getSpriteRef()).isNull();
    }

    @Test
    void deveConverterTiposDaPokeApi() {
        assertThat(mapper.converterTipo("grass")).isEqualTo(PokemonType.GRASS);
        assertThat(mapper.converterTipo("poison")).isEqualTo(PokemonType.POISON);
        assertThat(mapper.converterTipo("electric")).isEqualTo(PokemonType.ELECTRIC);
    }

    @Test
    void deveFalharQuandoStatObrigatorioAusente() {
        PokeApiPokemonResponse response = new PokeApiPokemonResponse(
                25,
                "pikachu",
                List.of(tipo(1, "electric")),
                List.of(stat("hp", 35)),
                new PokeApiSpriteResponse(null)
        );

        assertThatThrownBy(() -> mapper.paraSpecies(response))
                .isInstanceOf(PokeApiIntegracaoException.class)
                .hasMessageContaining("attack");
    }

    @Test
    void deveFalharQuandoTipoForDesconhecido() {
        assertThatThrownBy(() -> mapper.converterTipo("shadow"))
                .isInstanceOf(PokeApiIntegracaoException.class)
                .hasMessageContaining("Tipo desconhecido");
    }

    private PokeApiPokemonResponse responseBulbasaur() {
        return new PokeApiPokemonResponse(
                1,
                "bulbasaur",
                List.of(tipo(2, "poison"), tipo(1, "grass")),
                stats(45, 49, 49, 65, 65, 45),
                new PokeApiSpriteResponse("https://img/bulbasaur.png")
        );
    }

    private List<PokeApiStatResponse> stats(int hp, int attack, int defense, int specialAttack, int specialDefense, int speed) {
        return List.of(
                stat("hp", hp),
                stat("attack", attack),
                stat("defense", defense),
                stat("special-attack", specialAttack),
                stat("special-defense", specialDefense),
                stat("speed", speed)
        );
    }

    private PokeApiStatResponse stat(String name, int baseStat) {
        return new PokeApiStatResponse(baseStat, new PokeApiNamedResourceResponse(name, "https://pokeapi.co/" + name));
    }

    private PokeApiTypeResponse tipo(int slot, String name) {
        return new PokeApiTypeResponse(slot, new PokeApiNamedResourceResponse(name, "https://pokeapi.co/" + name));
    }
}
