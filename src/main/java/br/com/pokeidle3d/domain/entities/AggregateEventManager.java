package br.com.pokeidle3d.domain.entities;

import br.com.pokeidle3d.domain.events.DomainEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateEventManager implements IAggregate {

    private final List<DomainEvent> events = new ArrayList<>();

    @Override
    public List<DomainEvent> events() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void addEvent(DomainEvent event) {
        events.add(event);
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

    protected void registrarEvento(DomainEvent event) {
        addEvent(event);
    }

    public List<DomainEvent> eventosDominio() {
        return events();
    }

    public void limparEventosDominio() {
        clearEvents();
    }
}
