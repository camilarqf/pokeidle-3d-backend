package br.com.pokeidle3d.application.usecases.criarmove;

import br.com.pokeidle3d.application.bus.CommandHandler;
import br.com.pokeidle3d.domain.entities.Move;

public interface CriarMoveUseCase extends CommandHandler<CriarMoveCommand, Move> {
}
