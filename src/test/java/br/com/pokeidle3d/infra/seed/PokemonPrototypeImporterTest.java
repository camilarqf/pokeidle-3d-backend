package br.com.pokeidle3d.infra.seed;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.infra.integrations.pokeapi.PokeApiClient;
import br.com.pokeidle3d.infra.integrations.pokeapi.PokeApiIntegracaoException;
import br.com.pokeidle3d.infra.integrations.pokeapi.PokeApiPokemonResponse;
import br.com.pokeidle3d.infra.integrations.pokeapi.PokeApiSpeciesMapper;
import br.com.pokeidle3d.infra.mappers.SpeciesJpaMapper;
import br.com.pokeidle3d.infra.repositories.SpringDataSpeciesJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PokemonPrototypeImporterTest {

    @Test
    void naoDeveDuplicarSpeciesExistente() {
        PokeApiClient client = mock(PokeApiClient.class);
        PokeApiSpeciesMapper mapper = mock(PokeApiSpeciesMapper.class);
        SpeciesJpaMapper speciesJpaMapper = mock(SpeciesJpaMapper.class);
        SpringDataSpeciesJpaRepository repository = mock(SpringDataSpeciesJpaRepository.class);
        Species species = species(1, "bulbasaur");

        when(client.buscarPokemon("bulbasaur")).thenReturn(mock(PokeApiPokemonResponse.class));
        when(mapper.paraSpecies(any())).thenReturn(species);
        when(repository.existsByPokedexNumber(1)).thenReturn(true);

        new PokemonPrototypeImporter(client, mapper, speciesJpaMapper, repository)
                .run(new DefaultApplicationArguments());

        verify(repository, never()).save(any());
    }

    @Test
    void deveContinuarImportandoAposFalhaEmUmaSpecies() {
        PokeApiClient client = mock(PokeApiClient.class);
        PokeApiSpeciesMapper mapper = mock(PokeApiSpeciesMapper.class);
        SpeciesJpaMapper speciesJpaMapper = new SpeciesJpaMapper();
        SpringDataSpeciesJpaRepository repository = mock(SpringDataSpeciesJpaRepository.class);

        when(client.buscarPokemon("bulbasaur")).thenThrow(new PokeApiIntegracaoException("erro simulado"));
        when(client.buscarPokemon("charmander")).thenReturn(mock(PokeApiPokemonResponse.class));
        when(mapper.paraSpecies(any())).thenReturn(species(4, "charmander"));
        when(repository.existsByPokedexNumber(4)).thenReturn(false);
        when(repository.existsByNameIgnoreCase("charmander")).thenReturn(false);

        new PokemonPrototypeImporter(client, mapper, speciesJpaMapper, repository)
                .run(new DefaultApplicationArguments());

        verify(client).buscarPokemon("bulbasaur");
        verify(client).buscarPokemon("charmander");
        verify(repository, atLeastOnce()).save(any());
    }

    private Species species(Integer pokedexNumber, String name) {
        return Species.restaurar(
                null,
                pokedexNumber,
                name,
                PokemonType.FIRE,
                null,
                39,
                52,
                43,
                60,
                50,
                65,
                "sprite",
                "pokemon-3d-api:" + name,
                null,
                null
        );
    }
}
