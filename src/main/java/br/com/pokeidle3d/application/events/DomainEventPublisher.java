package br.com.pokeidle3d.application.events;

import br.com.pokeidle3d.domain.events.DomainEvent;

import java.util.Collection;

public interface DomainEventPublisher {

    void publicar(Collection<DomainEvent> eventos);
}
