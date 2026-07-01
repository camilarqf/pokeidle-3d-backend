package br.com.pokeidle3d.application.usecases.removemovefromspeciesmoveset;

import br.com.pokeidle3d.application.bus.Command;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;

public record RemoveMoveFromSpeciesMovesetCommand(
        Long speciesId,
        Long moveId,
        CorrelationKey correlationKey
) implements Command<Void> {
}
