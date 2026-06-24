package br.com.pokeidle3d.infra.repositories;

import br.com.pokeidle3d.domain.eventstore.entities.Event;
import br.com.pokeidle3d.domain.eventstore.repositories.EventRepository;
import br.com.pokeidle3d.infra.mappers.EventJpaMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepositoryJpa implements EventRepository {

    private final SpringDataEventJpaRepository repository;
    private final EventJpaMapper mapper;

    public EventRepositoryJpa(SpringDataEventJpaRepository repository, EventJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Event salvar(Event event) {
        return mapper.paraDominio(repository.save(mapper.paraJpa(event)));
    }
}
