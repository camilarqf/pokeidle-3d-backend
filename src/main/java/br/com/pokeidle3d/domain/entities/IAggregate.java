package br.com.pokeidle3d.domain.entities;

import br.com.pokeidle3d.domain.events.DomainEvent;

import java.util.List;

public interface IAggregate {

    List<DomainEvent> events();

    void addEvent(DomainEvent event);

    void clearEvents();
}
