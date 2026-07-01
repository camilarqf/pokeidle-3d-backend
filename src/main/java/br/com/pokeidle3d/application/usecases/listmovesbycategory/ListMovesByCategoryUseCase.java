package br.com.pokeidle3d.application.usecases.listmovesporcategory;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;

public interface ListMovesByCategoryUseCase extends QueryHandler<ListMovesByCategoryQuery, PaginatedResult<Move>> {
}
