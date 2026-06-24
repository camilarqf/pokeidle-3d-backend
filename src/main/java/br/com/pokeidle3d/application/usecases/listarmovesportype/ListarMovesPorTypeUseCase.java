package br.com.pokeidle3d.application.usecases.listarmovesportype;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public interface ListarMovesPorTypeUseCase extends QueryHandler<ListarMovesPorTypeQuery, ResultadoPaginado<Move>> {
}
