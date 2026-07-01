package br.com.pokeidle3d.application.usecases.listmoves;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;

public interface ListMovesUseCase extends QueryHandler<ListMovesQuery, PaginatedResult<Move>> {
}
