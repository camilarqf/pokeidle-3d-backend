package br.com.pokeidle3d.domain.eventstore.repositories;

import br.com.pokeidle3d.domain.eventstore.entities.Event;

public interface EventRepository {

    Event salvar(Event event);
}
