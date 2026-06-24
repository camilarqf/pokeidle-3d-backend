package br.com.pokeidle3d.application.eventhandlers;

import br.com.pokeidle3d.domain.events.MoveAdicionadoAoMovesetSpeciesEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MoveAdicionadoAoMovesetSpeciesEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveAdicionadoAoMovesetSpeciesEventHandler.class);

    @EventListener
    public void handle(MoveAdicionadoAoMovesetSpeciesEvent event) {
        LOGGER.info(
                "Reacao a MoveAdicionadoAoMovesetSpeciesEvent executada: aggregateId={}, speciesId={}, moveId={}, learnMethod={}, levelLearnedAt={}, correlationKey={}",
                event.aggregateId(),
                event.speciesId(),
                event.moveId(),
                event.learnMethod(),
                event.levelLearnedAt(),
                event.correlationKey().value()
        );
    }
}
