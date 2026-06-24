package br.com.pokeidle3d.application.usecases.adicionarmoveaomovesetspecies;

import br.com.pokeidle3d.application.bus.Command;
import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;

public record AdicionarMoveAoMovesetSpeciesCommand(
        Long speciesId,
        Long moveId,
        MoveLearnMethod learnMethod,
        Integer levelLearnedAt,
        CorrelationKey correlationKey
) implements Command<SpeciesMove> {
}
