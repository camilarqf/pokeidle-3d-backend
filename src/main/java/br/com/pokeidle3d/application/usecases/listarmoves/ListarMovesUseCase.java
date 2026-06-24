package br.com.pokeidle3d.application.usecases.listarmoves;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public interface ListarMovesUseCase extends QueryHandler<ListarMovesQuery, ResultadoPaginado<Move>> {
}
