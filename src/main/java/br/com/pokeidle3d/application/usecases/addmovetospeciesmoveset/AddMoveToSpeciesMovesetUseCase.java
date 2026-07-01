package br.com.pokeidle3d.application.usecases.addmovetospeciesmoveset;

import br.com.pokeidle3d.application.bus.CommandHandler;
import br.com.pokeidle3d.domain.entities.SpeciesMove;

public interface AddMoveToSpeciesMovesetUseCase extends CommandHandler<AddMoveToSpeciesMovesetCommand, SpeciesMove> {
}
