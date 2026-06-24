package br.com.pokeidle3d.application.events;

import br.com.pokeidle3d.domain.events.DomainEvent;

import java.util.Map;

public class DomainNotification {

    private final DomainEvent event;

    public DomainNotification(DomainEvent event) {
        this.event = event;
    }

    public String fullName() {
        return event.getClass().getName();
    }

    public String aggregateType() {
        return event.aggregateType();
    }

    public String userName() {
        return event.userName();
    }

    public DomainEvent metadata() {
        return event;
    }

    public Map<String, Object> toMap() {
        return event.paraMapa();
    }
}
