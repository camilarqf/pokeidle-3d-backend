package br.com.pokeidle3d.infra.seed;

import br.com.pokeidle3d.infra.integrations.pokemon3dapi.Pokemon3dApiClient;
import br.com.pokeidle3d.infra.integrations.pokemon3dapi.Pokemon3dAssetStorage;
import br.com.pokeidle3d.infra.integrations.pokemon3dapi.Pokemon3dModelRef;
import br.com.pokeidle3d.infra.persistence.SpeciesJpaEntity;
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
@Order(30)
@ConditionalOnProperty(prefix = "poke-idle-3d.pokemon-3d-import", name = "enabled", havingValue = "true")
public class Pokemon3dPrototypeAssetImporter implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Pokemon3dPrototypeAssetImporter.class);
    private static final List<String> SPECIES_TESTE = List.of("bulbasaur", "charmander");

    private final SpringDataSpeciesJpaRepository speciesRepository;
    private final Pokemon3dApiClient pokemon3dApiClient;
    private final Pokemon3dAssetStorage assetStorage;

    public Pokemon3dPrototypeAssetImporter(
            SpringDataSpeciesJpaRepository speciesRepository,
            Pokemon3dApiClient pokemon3dApiClient,
            Pokemon3dAssetStorage assetStorage
    ) {
        this.speciesRepository = speciesRepository;
        this.pokemon3dApiClient = pokemon3dApiClient;
        this.assetStorage = assetStorage;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        SPECIES_TESTE.forEach(this::importarModeloSeSpeciesExistir);
    }

    private void importarModeloSeSpeciesExistir(String speciesName) {
        try {
            SpeciesJpaEntity species = speciesRepository.findByNameIgnoreCase(speciesName).orElse(null);
            if (species == null) {
                LOGGER.warn("Species nao encontrada para importar asset 3D: name={}", speciesName);
                return;
            }

            Pokemon3dModelRef modelRef = pokemon3dApiClient.buscarModeloRegular(species.getPokedexNumber());
            String localPath = assetStorage.salvarModeloRegular(
                    species.getPokedexNumber(),
                    pokemon3dApiClient.baixarModelo(modelRef.modelUri())
            );
            speciesRepository.save(new SpeciesJpaEntity(
                    species.getId(),
                    species.getPokedexNumber(),
                    species.getName(),
                    species.getPrimaryType(),
                    species.getSecondaryType(),
                    species.getBaseHp(),
                    species.getBaseAttack(),
                    species.getBaseDefense(),
                    species.getBaseSpecialAttack(),
                    species.getBaseSpecialDefense(),
                    species.getBaseSpeed(),
                    species.getSpriteRef(),
                    localPath,
                    species.getCreatedAt(),
                    species.getUpdatedAt()
            ));
            LOGGER.info("Modelo 3D importado: species={}, path={}", species.getName(), localPath);
        } catch (RuntimeException exception) {
            LOGGER.warn("Falha ao importar modelo 3D: species={}, erro={}", speciesName, exception.getMessage());
        }
    }
}
