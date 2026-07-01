package br.com.pokeidle3d.infra.seed;

import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.infra.integrations.pokemon3dapi.Pokemon3dApiClient;
import br.com.pokeidle3d.infra.integrations.pokemon3dapi.Pokemon3dAssetStorage;
import br.com.pokeidle3d.infra.integrations.pokemon3dapi.Pokemon3dApiIntegrationException;
import br.com.pokeidle3d.infra.integrations.pokemon3dapi.Pokemon3dModelRef;
import br.com.pokeidle3d.infra.persistence.SpeciesJpaEntity;
import br.com.pokeidle3d.infra.repositories.SpringDataSpeciesJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class Pokemon3dPrototypeAssetImporterTest {

    @Test
    void deveBaixarModeloEAtualizarModel3dRefDaSpecies() {
        SpringDataSpeciesJpaRepository repository = mock(SpringDataSpeciesJpaRepository.class);
        Pokemon3dApiClient client = mock(Pokemon3dApiClient.class);
        Pokemon3dAssetStorage storage = mock(Pokemon3dAssetStorage.class);
        SpeciesJpaEntity bulbasaur = species(1L, 1, "bulbasaur");

        when(repository.findByNameIgnoreCase("bulbasaur")).thenReturn(Optional.of(bulbasaur));
        when(client.buscarModeloRegular(1)).thenReturn(new Pokemon3dModelRef(1, "Bulbasaur", "regular", URI.create("https://assets.test/1.glb")));
        when(client.baixarModelo(URI.create("https://assets.test/1.glb"))).thenReturn(new byte[]{1, 2, 3});
        when(storage.salvarModeloRegular(1, new byte[]{1, 2, 3})).thenReturn("storage/assets/3d/pokemon/regular/1.glb");

        new Pokemon3dPrototypeAssetImporter(repository, client, storage).run(new DefaultApplicationArguments());

        verify(repository, atLeastOnce()).save(any(SpeciesJpaEntity.class));
    }

    @Test
    void deveContinuarAposFalhaEmUmaSpecies() {
        SpringDataSpeciesJpaRepository repository = mock(SpringDataSpeciesJpaRepository.class);
        Pokemon3dApiClient client = mock(Pokemon3dApiClient.class);
        Pokemon3dAssetStorage storage = mock(Pokemon3dAssetStorage.class);

        when(repository.findByNameIgnoreCase("bulbasaur")).thenReturn(Optional.of(species(1L, 1, "bulbasaur")));
        when(repository.findByNameIgnoreCase("charmander")).thenReturn(Optional.of(species(2L, 4, "charmander")));
        when(client.buscarModeloRegular(1)).thenThrow(new Pokemon3dApiIntegrationException("erro simulado"));
        when(client.buscarModeloRegular(4)).thenReturn(new Pokemon3dModelRef(4, "Charmander", "regular", URI.create("https://assets.test/4.glb")));
        when(client.baixarModelo(URI.create("https://assets.test/4.glb"))).thenReturn(new byte[]{4});
        when(storage.salvarModeloRegular(4, new byte[]{4})).thenReturn("storage/assets/3d/pokemon/regular/4.glb");

        new Pokemon3dPrototypeAssetImporter(repository, client, storage).run(new DefaultApplicationArguments());

        verify(client).buscarModeloRegular(1);
        verify(client).buscarModeloRegular(4);
        verify(repository, atLeastOnce()).save(any(SpeciesJpaEntity.class));
    }

    private SpeciesJpaEntity species(Long id, Integer pokedexNumber, String name) {
        return new SpeciesJpaEntity(
                id,
                pokedexNumber,
                name,
                PokemonType.GRASS,
                null,
                45,
                49,
                49,
                65,
                65,
                45,
                "sprite",
                null,
                Instant.now(),
                Instant.now()
        );
    }
}
