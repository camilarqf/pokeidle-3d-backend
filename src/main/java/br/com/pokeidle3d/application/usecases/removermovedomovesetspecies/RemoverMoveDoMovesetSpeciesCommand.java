package br.com.pokeidle3d.application.usecases.removermovedomovesetspecies;

import br.com.pokeidle3d.application.bus.Command;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;

public record RemoverMoveDoMovesetSpeciesCommand(
        Long speciesId,
        Long moveId,
        CorrelationKey correlationKey
) implements Command<Void> {
}
