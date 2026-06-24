package br.com.pokeidle3d.application.usecases.listarmovesportype;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public interface ListarMovesPorTypeUseCase {

    ResultadoPaginado<Move> handle(ListarMovesPorTypeQuery query);
}
