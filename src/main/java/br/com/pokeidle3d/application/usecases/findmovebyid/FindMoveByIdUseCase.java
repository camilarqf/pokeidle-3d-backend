package br.com.pokeidle3d.application.usecases.findmovebyid;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Move;

public interface FindMoveByIdUseCase extends QueryHandler<FindMoveByIdQuery, Move> {
}
