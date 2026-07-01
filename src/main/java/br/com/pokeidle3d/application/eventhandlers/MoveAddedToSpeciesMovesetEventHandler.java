package br.com.pokeidle3d.application.eventhandlers;

import br.com.pokeidle3d.domain.events.MoveAddedToSpeciesMovesetEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MoveAddedToSpeciesMovesetEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveAddedToSpeciesMovesetEventHandler.class);

    @EventListener
    public void handle(MoveAddedToSpeciesMovesetEvent event) {
        LOGGER.info(
                "Reacao a MoveAddedToSpeciesMovesetEvent executada: aggregateId={}, speciesId={}, moveId={}, learnMethod={}, levelLearnedAt={}, correlationKey={}",
                event.aggregateId(),
                event.speciesId(),
                event.moveId(),
                event.learnMethod(),
                event.levelLearnedAt(),
                event.correlationKey().value()
        );
    }
}
