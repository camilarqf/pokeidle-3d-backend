package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integrations.pokemon-3d-api")
public record Pokemon3dApiProperties(String baseUrl, String assetsDirectory) {

    public Pokemon3dApiProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://pokemon-3d-api.onrender.com/v1";
        }
        if (assetsDirectory == null || assetsDirectory.isBlank()) {
            assetsDirectory = "storage/assets/3d/pokemon";
        }
    }
}
