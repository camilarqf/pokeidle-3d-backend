package br.com.pokeidle3d.api.mappers;

import br.com.pokeidle3d.api.contracts.SpeciesResponse;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpeciesApiMapperTest {

    private final SpeciesApiMapper mapper = new SpeciesApiMapper();

    @Test
    void deveResolverReferenciaSimbolicaDaPokemon3dApiParaUrlGlb() {
        SpeciesResponse response = mapper.paraResponse(species("pokemon-3d-api:bulbasaur"));

        assertThat(response.model3dRef())
                .isEqualTo("https://raw.githubusercontent.com/Pokemon-3D-api/assets/main/models/opt/regular/1.glb");
    }

    @Test
    void deveResolverCaminhoLocalDoStorageParaUrlPublica() {
        SpeciesResponse response = mapper.paraResponse(species("storage/assets/3d/pokemon/regular/1.glb"));

        assertThat(response.model3dRef()).isEqualTo("/assets/3d/pokemon/regular/1.glb");
    }

    @Test
    void devePreservarUrlAbsolutaDeModelo3d() {
        SpeciesResponse response = mapper.paraResponse(species("https://cdn.test/models/1.glb"));

        assertThat(response.model3dRef()).isEqualTo("https://cdn.test/models/1.glb");
    }

    private Species species(String model3dRef) {
        return Species.restaurar(
                1L,
                1,
                "bulbasaur",
                PokemonType.GRASS,
                PokemonType.POISON,
                45,
                49,
                49,
                65,
                65,
                45,
                "sprite",
                model3dRef,
                null,
                null
        );
    }
}
