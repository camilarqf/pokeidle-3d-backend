package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(Pokemon3dApiProperties.class)
public class Pokemon3dApiConfig {

    @Bean
    RestClient pokemon3dApiRestClient(Pokemon3dApiProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }
}
