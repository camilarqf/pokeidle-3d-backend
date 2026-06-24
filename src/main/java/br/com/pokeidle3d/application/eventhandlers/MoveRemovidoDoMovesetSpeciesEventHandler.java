package br.com.pokeidle3d.application.eventhandlers;

import br.com.pokeidle3d.domain.events.MoveRemovidoDoMovesetSpeciesEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MoveRemovidoDoMovesetSpeciesEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveRemovidoDoMovesetSpeciesEventHandler.class);

    @EventListener
    public void handle(MoveRemovidoDoMovesetSpeciesEvent event) {
        LOGGER.info(
                "Reacao a MoveRemovidoDoMovesetSpeciesEvent executada: aggregateId={}, speciesId={}, moveId={}, learnMethod={}, levelLearnedAt={}, correlationKey={}",
                event.aggregateId(),
                event.speciesId(),
                event.moveId(),
                event.learnMethod(),
                event.levelLearnedAt(),
                event.correlationKey().value()
        );
    }
}
