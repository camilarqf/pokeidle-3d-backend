package br.com.pokeidle3d.application.events;

import br.com.pokeidle3d.domain.entities.IAggregate;

public interface DomainEventUnitOfWork extends UnitOfWork {

    void registrar(IAggregate aggregate, Object aggregateId);

    void publicarEventosPendentes();

    @Override
    default void register(IAggregate aggregate, Object aggregateId) {
        registrar(aggregate, aggregateId);
    }

    @Override
    default void saveChanges() {
        publicarEventosPendentes();
    }
}
