package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class Pokemon3dAssetStorage {

    private final Pokemon3dApiProperties properties;

    public Pokemon3dAssetStorage(Pokemon3dApiProperties properties) {
        this.properties = properties;
    }

    public String salvarModeloRegular(Integer pokedexNumber, byte[] bytes) {
        Path baseDirectory = Path.of(properties.assetsDirectory());
        Path target = baseDirectory.resolve("regular").resolve(pokedexNumber + ".glb");
        try {
            Files.createDirectories(target.getParent());
            Files.write(target, bytes);
            return target.normalize().toString().replace('\\', '/');
        } catch (IOException exception) {
            throw new Pokemon3dApiIntegrationException("Erro ao salvar modelo 3D local: " + target, exception);
        }
    }
}
