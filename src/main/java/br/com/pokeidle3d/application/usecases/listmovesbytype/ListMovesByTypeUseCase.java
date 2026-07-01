package br.com.pokeidle3d.application.usecases.listmovesportype;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;

public interface ListMovesByTypeUseCase extends QueryHandler<ListMovesByTypeQuery, PaginatedResult<Move>> {
}
