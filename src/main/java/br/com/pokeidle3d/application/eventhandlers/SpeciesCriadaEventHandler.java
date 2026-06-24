package br.com.pokeidle3d.application.eventhandlers;

import br.com.pokeidle3d.domain.events.SpeciesCriadaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpeciesCriadaEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeciesCriadaEventHandler.class);

    @EventListener
    public void handle(SpeciesCriadaEvent event) {
        LOGGER.info(
                "Reacao a SpeciesCriadaEvent executada: aggregateId={}, pokedexNumber={}, name={}, correlationKey={}",
                event.aggregateId(),
                event.pokedexNumber(),
                event.name(),
                event.correlationKey()
        );
    }
}
