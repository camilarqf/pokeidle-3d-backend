package br.com.pokeidle3d.infra.seed;

import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.infra.persistence.MoveJpaEntity;
import br.com.pokeidle3d.infra.persistence.SpeciesJpaEntity;
import br.com.pokeidle3d.infra.persistence.SpeciesMoveJpaEntity;
import br.com.pokeidle3d.infra.repositories.SpringDataMoveJpaRepository;
import br.com.pokeidle3d.infra.repositories.SpringDataSpeciesJpaRepository;
import br.com.pokeidle3d.infra.repositories.SpringDataSpeciesMoveJpaRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(20)
@ConditionalOnProperty(prefix = "poke-idle-3d.seed", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DatasetMinimoJogoSeed implements ApplicationRunner {

    private final SpringDataSpeciesJpaRepository speciesRepository;
    private final SpringDataMoveJpaRepository moveRepository;
    private final SpringDataSpeciesMoveJpaRepository speciesMoveRepository;

    public DatasetMinimoJogoSeed(
            SpringDataSpeciesJpaRepository speciesRepository,
            SpringDataMoveJpaRepository moveRepository,
            SpringDataSpeciesMoveJpaRepository speciesMoveRepository
    ) {
        this.speciesRepository = speciesRepository;
        this.moveRepository = moveRepository;
        this.speciesMoveRepository = speciesMoveRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        popularMoves();
        popularMovesets();
    }

    private void popularMoves() {
        criarMoveSeNaoExistir("tackle", PokemonType.NORMAL, 40, 100, MoveCategory.PHYSICAL, 35);
        criarMoveSeNaoExistir("growl", PokemonType.NORMAL, null, 100, MoveCategory.STATUS, 40);
        criarMoveSeNaoExistir("vine-whip", PokemonType.GRASS, 45, 100, MoveCategory.PHYSICAL, 25);
        criarMoveSeNaoExistir("ember", PokemonType.FIRE, 40, 100, MoveCategory.SPECIAL, 25);
        criarMoveSeNaoExistir("water-gun", PokemonType.WATER, 40, 100, MoveCategory.SPECIAL, 25);
        criarMoveSeNaoExistir("thunder-shock", PokemonType.ELECTRIC, 40, 100, MoveCategory.SPECIAL, 30);
        criarMoveSeNaoExistir("quick-attack", PokemonType.NORMAL, 40, 100, MoveCategory.PHYSICAL, 30);
        criarMoveSeNaoExistir("scratch", PokemonType.NORMAL, 40, 100, MoveCategory.PHYSICAL, 35);
        criarMoveSeNaoExistir("tail-whip", PokemonType.NORMAL, null, 100, MoveCategory.STATUS, 30);
        criarMoveSeNaoExistir("gust", PokemonType.FLYING, 40, 100, MoveCategory.SPECIAL, 35);
        criarMoveSeNaoExistir("bite", PokemonType.DARK, 60, 100, MoveCategory.PHYSICAL, 25);
        criarMoveSeNaoExistir("leer", PokemonType.NORMAL, null, 100, MoveCategory.STATUS, 30);
    }

    private void popularMovesets() {
        associarLevelUp("bulbasaur", "tackle", 1);
        associarLevelUp("bulbasaur", "growl", 1);
        associarLevelUp("bulbasaur", "vine-whip", 7);

        associarLevelUp("charmander", "scratch", 1);
        associarLevelUp("charmander", "growl", 1);
        associarLevelUp("charmander", "ember", 7);

        associarLevelUp("squirtle", "tackle", 1);
        associarLevelUp("squirtle", "tail-whip", 1);
        associarLevelUp("squirtle", "water-gun", 7);

        associarLevelUp("pikachu", "thunder-shock", 1);
        associarLevelUp("pikachu", "growl", 1);
        associarLevelUp("pikachu", "quick-attack", 7);

        associarLevelUp("pidgey", "tackle", 1);
        associarLevelUp("pidgey", "gust", 7);
        associarLevelUp("pidgey", "quick-attack", 9);

        associarLevelUp("rattata", "tackle", 1);
        associarLevelUp("rattata", "tail-whip", 1);
        associarLevelUp("rattata", "bite", 10);
    }

    private void criarMoveSeNaoExistir(
            String name,
            PokemonType type,
            Integer power,
            Integer accuracy,
            MoveCategory category,
            Integer pp
    ) {
        if (moveRepository.existsByNameIgnoreCase(name)) {
            return;
        }

        moveRepository.save(new MoveJpaEntity(null, name, type, power, accuracy, category, pp, null, null));
    }

    private void associarLevelUp(String speciesName, String moveName, Integer levelLearnedAt) {
        SpeciesJpaEntity species = speciesRepository.findByNameIgnoreCase(speciesName).orElse(null);
        MoveJpaEntity move = moveRepository.findByNameIgnoreCase(moveName).orElse(null);
        if (species == null || move == null) {
            return;
        }

        if (speciesMoveRepository.existsBySpeciesIdAndMoveIdAndLearnMethodAndLevelLearnedAt(
                species.getId(),
                move.getId(),
                MoveLearnMethod.LEVEL_UP,
                levelLearnedAt
        )) {
            return;
        }

        speciesMoveRepository.save(new SpeciesMoveJpaEntity(
                null,
                species.getId(),
                move.getId(),
                MoveLearnMethod.LEVEL_UP,
                levelLearnedAt,
                null,
                null
        ));
    }
}
