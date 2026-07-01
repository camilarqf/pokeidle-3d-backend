package br.com.pokeidle3d.application.eventhandlers;

import br.com.pokeidle3d.domain.events.SpeciesCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpeciesCreatedEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeciesCreatedEventHandler.class);

    @EventListener
    public void handle(SpeciesCreatedEvent event) {
        LOGGER.info(
                "Reacao a SpeciesCreatedEvent executada: aggregateId={}, pokedexNumber={}, name={}, correlationKey={}",
                event.aggregateId(),
                event.pokedexNumber(),
                event.name(),
                event.correlationKey()
        );
    }
}
