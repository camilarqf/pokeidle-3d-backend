package br.com.pokeidle3d.application.usecases.listarmovesporcategory;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public interface ListarMovesPorCategoryUseCase {

    ResultadoPaginado<Move> handle(ListarMovesPorCategoryQuery query);
}
