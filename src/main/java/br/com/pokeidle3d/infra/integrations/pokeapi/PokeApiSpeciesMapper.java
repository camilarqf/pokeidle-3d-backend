package br.com.pokeidle3d.infra.integrations.pokeapi;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class PokeApiSpeciesMapper {

    private static final String POKEMON_3D_ASSETS_BASE_URL =
            "https://raw.githubusercontent.com/Pokemon-3D-api/assets/main/models/opt/regular/";

    public Species paraSpecies(PokeApiPokemonResponse response) {
        validarResponse(response);

        List<PokeApiTypeResponse> types = response.types()
                .stream()
                .sorted(Comparator.comparing(PokeApiTypeResponse::slot))
                .toList();

        PokemonType primaryType = converterTipo(types.getFirst().type().name());
        PokemonType secondaryType = types.size() > 1 ? converterTipo(types.get(1).type().name()) : null;

        return Species.restaurar(
                null,
                response.id(),
                response.name(),
                primaryType,
                secondaryType,
                statObrigatorio(response, "hp"),
                statObrigatorio(response, "attack"),
                statObrigatorio(response, "defense"),
                statObrigatorio(response, "special-attack"),
                statObrigatorio(response, "special-defense"),
                statObrigatorio(response, "speed"),
                response.sprites() == null ? null : response.sprites().front_default(),
                POKEMON_3D_ASSETS_BASE_URL + response.id() + ".glb",
                null,
                null
        );
    }

    public PokemonType converterTipo(String pokeApiType) {
        if (pokeApiType == null || pokeApiType.isBlank()) {
            throw new PokeApiIntegrationException("Tipo ausente na resposta da PokeAPI");
        }

        try {
            return PokemonType.valueOf(pokeApiType.trim().replace("-", "_").toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new PokeApiIntegrationException("Tipo desconhecido na PokeAPI: " + pokeApiType, exception);
        }
    }

    private void validarResponse(PokeApiPokemonResponse response) {
        if (response == null) {
            throw new PokeApiIntegrationException("Resposta nula da PokeAPI");
        }
        if (response.id() == null || response.name() == null || response.name().isBlank()) {
            throw new PokeApiIntegrationException("Pokemon sem id ou name na resposta da PokeAPI");
        }
        if (response.types() == null || response.types().isEmpty()) {
            throw new PokeApiIntegrationException("Pokemon sem tipos na resposta da PokeAPI: " + response.name());
        }
        if (response.stats() == null || response.stats().isEmpty()) {
            throw new PokeApiIntegrationException("Pokemon sem stats na resposta da PokeAPI: " + response.name());
        }
    }

    private Integer statObrigatorio(PokeApiPokemonResponse response, String statName) {
        return response.stats()
                .stream()
                .filter(stat -> stat.stat() != null && statName.equals(stat.stat().name()))
                .map(PokeApiStatResponse::base_stat)
                .findFirst()
                .orElseThrow(() -> new PokeApiIntegrationException("Stat obrigatorio ausente na PokeAPI: " + statName));
    }
}
