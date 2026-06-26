package br.com.pokeidle3d.api.config;

import br.com.pokeidle3d.infra.integrations.pokemon3dapi.Pokemon3dApiProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class Pokemon3dAssetsWebConfig implements WebMvcConfigurer {

    private final Pokemon3dApiProperties properties;

    public Pokemon3dAssetsWebConfig(Pokemon3dApiProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path assetsDirectory = Path.of(properties.assetsDirectory()).toAbsolutePath().normalize();
        registry.addResourceHandler("/assets/3d/pokemon/**")
                .addResourceLocations(assetsDirectory.toUri().toString() + "/");
    }
}
