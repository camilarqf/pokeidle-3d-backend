package br.com.pokeidle3d.infra.integrations.pokeapi;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integrations.pokeapi")
public record PokeApiProperties(String baseUrl) {

    public PokeApiProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://pokeapi.co/api/v2";
        }
    }
}
