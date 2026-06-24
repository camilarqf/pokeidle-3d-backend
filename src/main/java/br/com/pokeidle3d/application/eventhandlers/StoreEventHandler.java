package br.com.pokeidle3d.application.eventhandlers;

import br.com.pokeidle3d.application.events.DomainNotification;
import br.com.pokeidle3d.domain.eventstore.entities.Event;
import br.com.pokeidle3d.domain.eventstore.repositories.EventRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Component
public class StoreEventHandler {

    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public StoreEventHandler(EventRepository eventRepository, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(DomainNotification notification) throws Exception {
        Event event = Event.criar(
                notification.fullName(),
                notification.aggregateType(),
                objectMapper.writeValueAsString(notification.toMap()),
                notification.userName()
        );

        eventRepository.salvar(event);
    }
}
