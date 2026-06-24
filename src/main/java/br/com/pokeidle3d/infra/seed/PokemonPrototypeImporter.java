package br.com.pokeidle3d.infra.seed;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.infra.integrations.pokeapi.PokeApiClient;
import br.com.pokeidle3d.infra.integrations.pokeapi.PokeApiSpeciesMapper;
import br.com.pokeidle3d.infra.mappers.SpeciesJpaMapper;
import br.com.pokeidle3d.infra.repositories.SpringDataSpeciesJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Profile("dev")
@Order(10)
@ConditionalOnProperty(prefix = "poke-idle-3d.pokeapi-import", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PokemonPrototypeImporter implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(PokemonPrototypeImporter.class);
    private static final List<String> POKEMON_INICIAIS = List.of(
            "bulbasaur",
            "charmander",
            "squirtle",
            "pikachu",
            "pidgey",
            "rattata"
    );

    private final PokeApiClient pokeApiClient;
    private final PokeApiSpeciesMapper pokeApiSpeciesMapper;
    private final SpeciesJpaMapper speciesJpaMapper;
    private final SpringDataSpeciesJpaRepository speciesRepository;

    public PokemonPrototypeImporter(
            PokeApiClient pokeApiClient,
            PokeApiSpeciesMapper pokeApiSpeciesMapper,
            SpeciesJpaMapper speciesJpaMapper,
            SpringDataSpeciesJpaRepository speciesRepository
    ) {
        this.pokeApiClient = pokeApiClient;
        this.pokeApiSpeciesMapper = pokeApiSpeciesMapper;
        this.speciesJpaMapper = speciesJpaMapper;
        this.speciesRepository = speciesRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        POKEMON_INICIAIS.forEach(this::importarSeNaoExistir);
    }

    private void importarSeNaoExistir(String pokemonName) {
        try {
            Species species = pokeApiSpeciesMapper.paraSpecies(pokeApiClient.buscarPokemon(pokemonName));
            if (speciesRepository.existsByPokedexNumber(species.getPokedexNumber())
                    || speciesRepository.existsByNameIgnoreCase(species.getName())) {
                return;
            }

            speciesRepository.save(speciesJpaMapper.paraJpa(species));
            LOGGER.info("Species importada da PokeAPI: pokedexNumber={}, name={}", species.getPokedexNumber(), species.getName());
        } catch (RuntimeException exception) {
            LOGGER.warn("Falha ao importar Species da PokeAPI: name={}, erro={}", pokemonName, exception.getMessage());
        }
    }
}
