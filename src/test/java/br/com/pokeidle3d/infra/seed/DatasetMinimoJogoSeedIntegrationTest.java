package br.com.pokeidle3d.infra.seed;

import br.com.pokeidle3d.infra.repositories.SpringDataMoveJpaRepository;
import br.com.pokeidle3d.infra.repositories.SpringDataSpeciesJpaRepository;
import br.com.pokeidle3d.infra.repositories.SpringDataSpeciesMoveJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(properties = {
        "poke-idle-3d.seed.enabled=true",
        "spring.datasource.url=jdbc:h2:mem:pokeidle3d_seed;MODE=MSSQLServer;DATABASE_TO_UPPER=FALSE;DB_CLOSE_DELAY=-1"
})
class DatasetMinimoJogoSeedIntegrationTest {

    @Autowired
    private SpringDataSpeciesJpaRepository speciesRepository;

    @Autowired
    private SpringDataMoveJpaRepository moveRepository;

    @Autowired
    private SpringDataSpeciesMoveJpaRepository speciesMoveRepository;

    @Test
    void devePopularMovesSemCriarSpeciesManual() {
        assertThat(speciesRepository.count()).isZero();
        assertThat(moveRepository.count()).isEqualTo(12);
        assertThat(speciesMoveRepository.count()).isZero();

        assertThat(moveRepository.findByNameIgnoreCase("tackle")).isPresent();
        assertThat(moveRepository.findByNameIgnoreCase("ember")).isPresent();
        assertThat(moveRepository.findByNameIgnoreCase("thunder-shock")).isPresent();
    }
}
