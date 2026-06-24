package br.com.pokeidle3d.application.usecases.criarmove;

import br.com.pokeidle3d.domain.entities.Move;

public interface CriarMoveUseCase {

    Move handle(CriarMoveCommand command);
}
