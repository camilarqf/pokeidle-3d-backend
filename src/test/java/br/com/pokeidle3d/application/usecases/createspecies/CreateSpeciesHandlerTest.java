package br.com.pokeidle3d.application.usecases.createspecies;

import br.com.pokeidle3d.application.events.DomainEventUnitOfWork;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.entities.IAggregate;
import br.com.pokeidle3d.domain.events.SpeciesCreatedEvent;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CreateSpeciesHandlerTest {

    @Test
    void devePersistirPublicarEventosELimparEventosDoAggregate() {
        DomainEventUnitOfWorkFake unidadeTrabalho = new DomainEventUnitOfWorkFake();
        SpeciesRepositoryFake repository = new SpeciesRepositoryFake(unidadeTrabalho);
        CreateSpeciesHandler handler = new CreateSpeciesHandler(repository, unidadeTrabalho);
        CorrelationKey correlationKey = new CorrelationKey("handler-test-key");

        Species speciesSalva = handler.handle(new CreateSpeciesCommand(
                1,
                "Bulbasaur",
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
                correlationKey
        ));

        assertThat(speciesSalva.getId()).isEqualTo(1L);
        assertThat(repository.aggregateRecebido).isNotNull();
        assertThat(repository.aggregateRecebido.eventosDominio()).isEmpty();
        assertThat(unidadeTrabalho.aggregateRegistrado).isSameAs(repository.aggregateRecebido);
        assertThat(unidadeTrabalho.aggregateIdRegistrado).isEqualTo(1L);
        assertThat(unidadeTrabalho.publicouEventosPendentes).isTrue();
        assertThat(unidadeTrabalho.eventoPublicado).isInstanceOf(SpeciesCreatedEvent.class);
        assertThat(unidadeTrabalho.eventoPublicado.correlationKey()).isEqualTo(correlationKey);
        assertThat(unidadeTrabalho.eventoPublicado.aggregateId()).isEqualTo("1");
    }

    private static class DomainEventUnitOfWorkFake implements DomainEventUnitOfWork {

        private IAggregate aggregateRegistrado;
        private Object aggregateIdRegistrado;
        private SpeciesCreatedEvent eventoPublicado;
        private boolean publicouEventosPendentes;

        @Override
        public void registrar(IAggregate aggregate, Object aggregateId) {
            aggregateRegistrado = aggregate;
            aggregateIdRegistrado = aggregateId;
        }

        @Override
        public void publicarEventosPendentes() {
            publicouEventosPendentes = true;
            eventoPublicado = (SpeciesCreatedEvent) aggregateRegistrado.events().get(0);
            eventoPublicado.definirAggregateId(String.valueOf(aggregateIdRegistrado));
            aggregateRegistrado.clearEvents();
        }
    }

    private static class SpeciesRepositoryFake implements SpeciesRepository {

        private final DomainEventUnitOfWork unidadeTrabalho;
        private Species aggregateRecebido;

        private SpeciesRepositoryFake(DomainEventUnitOfWork unidadeTrabalho) {
            this.unidadeTrabalho = unidadeTrabalho;
        }

        @Override
        public Species salvar(Species species) {
            aggregateRecebido = species;
            Species speciesSalva = Species.restaurar(
                    1L,
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
                    species.getModel3dRef(),
                    Instant.now(),
                    Instant.now()
            );
            unidadeTrabalho.registrar(species, speciesSalva.getId());
            return speciesSalva;
        }

        @Override
        public Optional<Species> buscarPorId(Long id) {
            return Optional.empty();
        }

        @Override
        public Optional<Species> buscarPorPokedexNumber(Integer pokedexNumber) {
            return Optional.empty();
        }

        @Override
        public PaginatedResult<Species> listar(int pagina, int tamanho) {
            return new PaginatedResult<>(List.of(), 0, 0, pagina, tamanho);
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
}
