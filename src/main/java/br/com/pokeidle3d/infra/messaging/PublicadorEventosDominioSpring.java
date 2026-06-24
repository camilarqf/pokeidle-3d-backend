package br.com.pokeidle3d.infra.messaging;

import br.com.pokeidle3d.application.events.DomainNotification;
import br.com.pokeidle3d.application.events.PublicadorEventosDominio;
import br.com.pokeidle3d.domain.events.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PublicadorEventosDominioSpring implements PublicadorEventosDominio {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicadorEventosDominioSpring.class);

    private final ApplicationEventPublisher publisher;

    public PublicadorEventosDominioSpring(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publicar(Collection<DomainEvent> eventos) {
        eventos.forEach(evento -> {
            LOGGER.info(
                    "Publicando evento de dominio: tipo={}, aggregateType={}, aggregateId={}, eventId={}, correlationKey={}",
                    evento.getClass().getSimpleName(),
                    evento.aggregateType(),
                    evento.aggregateId(),
                    evento.eventId(),
                    evento.correlationKey()
            );
            publisher.publishEvent(new DomainNotification(evento));
            publisher.publishEvent(evento);
        });
    }
}
