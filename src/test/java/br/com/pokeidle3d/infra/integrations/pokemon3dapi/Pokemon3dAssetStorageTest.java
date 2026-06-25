package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class Pokemon3dAssetStorageTest {

    @TempDir
    private Path tempDir;

    @Test
    void deveSalvarModeloLocalmente() throws Exception {
        Pokemon3dAssetStorage storage = new Pokemon3dAssetStorage(
                new Pokemon3dApiProperties("https://pokemon3d.test/v1", tempDir.toString())
        );

        String path = storage.salvarModeloRegular(1, new byte[]{1, 2, 3});

        assertThat(path).endsWith("regular/1.glb");
        assertThat(Files.readAllBytes(Path.of(path))).containsExactly(1, 2, 3);
    }
}
