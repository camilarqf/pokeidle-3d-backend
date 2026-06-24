package br.com.pokeidle3d.application.events;

import br.com.pokeidle3d.domain.entities.IAggregate;

public interface UnitOfWork {

    void register(IAggregate aggregate, Object aggregateId);

    void saveChanges();
}
