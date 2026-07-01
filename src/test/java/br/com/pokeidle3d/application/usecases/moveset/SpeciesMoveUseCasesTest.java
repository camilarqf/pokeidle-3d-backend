package br.com.pokeidle3d.application.usecases.moveset;

import br.com.pokeidle3d.application.events.DomainEventUnitOfWork;
import br.com.pokeidle3d.application.usecases.addmovetospeciesmoveset.AddMoveToSpeciesMovesetCommand;
import br.com.pokeidle3d.application.usecases.addmovetospeciesmoveset.AddMoveToSpeciesMovesetHandler;
import br.com.pokeidle3d.application.usecases.listmovesetdaspecies.ListSpeciesMovesetHandler;
import br.com.pokeidle3d.application.usecases.listmovesetdaspecies.ListSpeciesMovesetQuery;
import br.com.pokeidle3d.application.usecases.listmovesetdaspecies.SpeciesMovesetItem;
import br.com.pokeidle3d.application.usecases.listspeciespormove.ListSpeciesByMoveHandler;
import br.com.pokeidle3d.application.usecases.listspeciespormove.ListSpeciesByMoveQuery;
import br.com.pokeidle3d.application.usecases.listspeciespormove.SpeciesByMoveItem;
import br.com.pokeidle3d.application.usecases.removemovefromspeciesmoveset.RemoveMoveFromSpeciesMovesetCommand;
import br.com.pokeidle3d.application.usecases.removemovefromspeciesmoveset.RemoveMoveFromSpeciesMovesetHandler;
import br.com.pokeidle3d.domain.entities.IAggregate;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.events.DomainEvent;
import br.com.pokeidle3d.domain.events.MoveAddedToSpeciesMovesetEvent;
import br.com.pokeidle3d.domain.events.MoveRemovedFromSpeciesMovesetEvent;
import br.com.pokeidle3d.domain.exceptions.DuplicateSpeciesMoveException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesMoveRepository;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpeciesMoveUseCasesTest {

    @Test
    void deveAdicionarMoveAoMovesetPublicarEventoELimparEventos() {
        FakeUnitOfWork unidadeTrabalho = new FakeUnitOfWork();
        SpeciesMoveRepositoryFake speciesMoveRepository = new SpeciesMoveRepositoryFake(unidadeTrabalho);
        AddMoveToSpeciesMovesetHandler handler = new AddMoveToSpeciesMovesetHandler(
                new SpeciesRepositoryFake(),
                new MoveRepositoryFake(),
                speciesMoveRepository,
                unidadeTrabalho
        );
        CorrelationKey correlationKey = new CorrelationKey("add-handler-key");

        SpeciesMove salvo = handler.handle(new AddMoveToSpeciesMovesetCommand(
                1L,
                2L,
                MoveLearnMethod.LEVEL_UP,
                7,
                correlationKey
        ));

        assertThat(salvo.getId()).isEqualTo(10L);
        assertThat(unidadeTrabalho.publicouEventosPendentes).isTrue();
        assertThat(unidadeTrabalho.eventoPublicado).isInstanceOf(MoveAddedToSpeciesMovesetEvent.class);
        assertThat(unidadeTrabalho.eventoPublicado.correlationKey()).isEqualTo(correlationKey);
        assertThat(unidadeTrabalho.eventoPublicado.aggregateId()).isEqualTo("10");
        assertThat(speciesMoveRepository.aggregateRecebido.events()).isEmpty();
    }

    @Test
    void naoDeveAdicionarAssociacaoDuplicada() {
        FakeUnitOfWork unidadeTrabalho = new FakeUnitOfWork();
        SpeciesMoveRepositoryFake speciesMoveRepository = new SpeciesMoveRepositoryFake(unidadeTrabalho);
        speciesMoveRepository.duplicado = true;
        AddMoveToSpeciesMovesetHandler handler = new AddMoveToSpeciesMovesetHandler(
                new SpeciesRepositoryFake(),
                new MoveRepositoryFake(),
                speciesMoveRepository,
                unidadeTrabalho
        );

        assertThatThrownBy(() -> handler.handle(new AddMoveToSpeciesMovesetCommand(
                1L,
                2L,
                MoveLearnMethod.TM,
                null,
                CorrelationKey.gerar()
        )))
                .isInstanceOf(DuplicateSpeciesMoveException.class)
                .hasMessageContaining("moveset");
    }

    @Test
    void deveRemoverMoveDoMovesetPublicarEventoELimparEventos() {
        FakeUnitOfWork unidadeTrabalho = new FakeUnitOfWork();
        SpeciesMoveRepositoryFake speciesMoveRepository = new SpeciesMoveRepositoryFake(unidadeTrabalho);
        RemoveMoveFromSpeciesMovesetHandler handler = new RemoveMoveFromSpeciesMovesetHandler(
                new SpeciesRepositoryFake(),
                new MoveRepositoryFake(),
                speciesMoveRepository,
                unidadeTrabalho
        );
        CorrelationKey correlationKey = new CorrelationKey("remove-handler-key");

        handler.handle(new RemoveMoveFromSpeciesMovesetCommand(1L, 2L, correlationKey));

        assertThat(speciesMoveRepository.removeu).isTrue();
        assertThat(unidadeTrabalho.publicouEventosPendentes).isTrue();
        assertThat(unidadeTrabalho.eventoPublicado).isInstanceOf(MoveRemovedFromSpeciesMovesetEvent.class);
        assertThat(unidadeTrabalho.eventoPublicado.correlationKey()).isEqualTo(correlationKey);
        assertThat(unidadeTrabalho.eventoPublicado.aggregateId()).isEqualTo("10");
    }

    @Test
    void deveListarMovesetDaSpecies() {
        SpeciesMoveRepositoryFake speciesMoveRepository = new SpeciesMoveRepositoryFake(new FakeUnitOfWork());
        ListSpeciesMovesetHandler handler = new ListSpeciesMovesetHandler(
                new SpeciesRepositoryFake(),
                new MoveRepositoryFake(),
                speciesMoveRepository
        );

        List<SpeciesMovesetItem> itens = handler.handle(new ListSpeciesMovesetQuery(1L));

        assertThat(itens).hasSize(1);
        assertThat(itens.get(0).speciesMove().getSpeciesId()).isEqualTo(1L);
        assertThat(itens.get(0).move().getName()).isEqualTo("tackle");
    }

    @Test
    void deveListarSpeciesPorMove() {
        SpeciesMoveRepositoryFake speciesMoveRepository = new SpeciesMoveRepositoryFake(new FakeUnitOfWork());
        ListSpeciesByMoveHandler handler = new ListSpeciesByMoveHandler(
                new SpeciesRepositoryFake(),
                new MoveRepositoryFake(),
                speciesMoveRepository
        );

        List<SpeciesByMoveItem> itens = handler.handle(new ListSpeciesByMoveQuery(2L));

        assertThat(itens).hasSize(1);
        assertThat(itens.get(0).speciesMove().getMoveId()).isEqualTo(2L);
        assertThat(itens.get(0).species().getName()).isEqualTo("bulbasaur");
    }

    private static class FakeUnitOfWork implements DomainEventUnitOfWork {

        private IAggregate aggregateRegistrado;
        private Object aggregateIdRegistrado;
        private DomainEvent eventoPublicado;
        private boolean publicouEventosPendentes;

        @Override
        public void registrar(IAggregate aggregate, Object aggregateId) {
            aggregateRegistrado = aggregate;
            aggregateIdRegistrado = aggregateId;
        }

        @Override
        public void publicarEventosPendentes() {
            publicouEventosPendentes = true;
            eventoPublicado = aggregateRegistrado.events().get(0);
            eventoPublicado.definirAggregateId(String.valueOf(aggregateIdRegistrado));
            aggregateRegistrado.clearEvents();
        }
    }

    private static class SpeciesMoveRepositoryFake implements SpeciesMoveRepository {

        private final DomainEventUnitOfWork unidadeTrabalho;
        private SpeciesMove aggregateRecebido;
        private boolean duplicado;
        private boolean removeu;
        private final SpeciesMove existente = SpeciesMove.restaurar(10L, 1L, 2L, MoveLearnMethod.LEVEL_UP, 7, Instant.now(), Instant.now());

        private SpeciesMoveRepositoryFake(DomainEventUnitOfWork unidadeTrabalho) {
            this.unidadeTrabalho = unidadeTrabalho;
        }

        @Override
        public SpeciesMove salvar(SpeciesMove speciesMove) {
            aggregateRecebido = speciesMove;
            SpeciesMove salvo = SpeciesMove.restaurar(
                    10L,
                    speciesMove.getSpeciesId(),
                    speciesMove.getMoveId(),
                    speciesMove.getLearnMethod(),
                    speciesMove.getLevelLearnedAt(),
                    Instant.now(),
                    Instant.now()
            );
            unidadeTrabalho.registrar(speciesMove, salvo.getId());
            return salvo;
        }

        @Override
        public void remover(SpeciesMove speciesMove) {
            removeu = true;
            unidadeTrabalho.registrar(speciesMove, speciesMove.getId());
        }

        @Override
        public Optional<SpeciesMove> buscarPorId(Long id) {
            return Optional.of(existente);
        }

        @Override
        public Optional<SpeciesMove> buscarPorChave(Long speciesId, Long moveId, MoveLearnMethod learnMethod, Integer levelLearnedAt) {
            return duplicado ? Optional.of(existente) : Optional.empty();
        }

        @Override
        public Optional<SpeciesMove> buscarPrimeiroPorSpeciesIdEMoveId(Long speciesId, Long moveId) {
            return Optional.of(existente);
        }

        @Override
        public List<SpeciesMove> listarPorSpeciesIdEMoveId(Long speciesId, Long moveId) {
            return List.of(existente);
        }

        @Override
        public List<SpeciesMove> listarPorSpeciesId(Long speciesId) {
            return List.of(existente);
        }

        @Override
        public List<SpeciesMove> listarPorMoveId(Long moveId) {
            return List.of(existente);
        }

        @Override
        public boolean existePorChave(Long speciesId, Long moveId, MoveLearnMethod learnMethod, Integer levelLearnedAt) {
            return duplicado;
        }
    }

    private static class SpeciesRepositoryFake implements SpeciesRepository {

        @Override
        public Species salvar(Species species) {
            return species;
        }

        @Override
        public Optional<Species> buscarPorId(Long id) {
            return Optional.of(Species.restaurar(
                    id,
                    1,
                    "bulbasaur",
                    PokemonType.GRASS,
                    PokemonType.POISON,
                    45,
                    49,
                    49,
                    65,
                    65,
                    45,
                    "sprite",
                    "modelo",
                    Instant.now(),
                    Instant.now()
            ));
        }

        @Override
        public Optional<Species> buscarPorPokedexNumber(Integer pokedexNumber) {
            return Optional.empty();
        }

        @Override
        public PaginatedResult<Species> listar(int pagina, int tamanho) {
            return new PaginatedResult<>(new ArrayList<>(), 0, 0, pagina, tamanho);
        }

        @Override
        public boolean existePorPokedexNumber(Integer pokedexNumber) {
            return false;
        }

        @Override
        public boolean existePorName(String name) {
            return false;
        }
    }

    private static class MoveRepositoryFake implements MoveRepository {

        @Override
        public Move salvar(Move move) {
            return move;
        }

        @Override
        public Optional<Move> buscarPorId(Long id) {
            return Optional.of(Move.restaurar(
                    id,
                    "tackle",
                    PokemonType.NORMAL,
                    40,
                    100,
                    MoveCategory.PHYSICAL,
                    35,
                    Instant.now(),
                    Instant.now()
            ));
        }

        @Override
        public Optional<Move> buscarPorName(String name) {
            return Optional.empty();
        }

        @Override
        public PaginatedResult<Move> listar(int pagina, int tamanho) {
            return new PaginatedResult<>(new ArrayList<>(), 0, 0, pagina, tamanho);
        }

        @Override
        public PaginatedResult<Move> listarPorType(PokemonType type, int pagina, int tamanho) {
            return new PaginatedResult<>(new ArrayList<>(), 0, 0, pagina, tamanho);
        }

        @Override
        public PaginatedResult<Move> listarPorCategory(MoveCategory category, int pagina, int tamanho) {
            return new PaginatedResult<>(new ArrayList<>(), 0, 0, pagina, tamanho);
        }

        @Override
        public boolean existePorName(String name) {
            return false;
        }
    }
}
