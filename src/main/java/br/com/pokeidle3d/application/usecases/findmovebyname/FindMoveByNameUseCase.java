package br.com.pokeidle3d.application.usecases.findmovebyname;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Move;

public interface FindMoveByNameUseCase extends QueryHandler<FindMoveByNameQuery, Move> {
}
