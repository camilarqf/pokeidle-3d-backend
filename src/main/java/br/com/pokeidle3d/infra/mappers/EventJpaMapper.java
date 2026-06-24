package br.com.pokeidle3d.infra.mappers;

import br.com.pokeidle3d.domain.eventstore.entities.Event;
import br.com.pokeidle3d.infra.persistence.EventJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EventJpaMapper {

    public EventJpaEntity paraJpa(Event event) {
        return new EventJpaEntity(
                event.getId(),
                event.getFullName(),
                event.getAggregateType(),
                event.getPayload(),
                event.getUserName(),
                event.getCreatedAt()
        );
    }

    public Event paraDominio(EventJpaEntity entity) {
        return new Event(
                entity.getId(),
                entity.getFullName(),
                entity.getAggregateType(),
                entity.getPayload(),
                entity.getUserName(),
                entity.getCreatedAt()
        );
    }
}
