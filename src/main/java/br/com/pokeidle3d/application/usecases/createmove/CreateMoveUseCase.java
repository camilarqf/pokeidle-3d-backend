package br.com.pokeidle3d.application.usecases.createmove;

import br.com.pokeidle3d.application.bus.CommandHandler;
import br.com.pokeidle3d.domain.entities.Move;

public interface CreateMoveUseCase extends CommandHandler<CreateMoveCommand, Move> {
}
