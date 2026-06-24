package br.com.pokeidle3d.application.usecases.listarmoves;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public interface ListarMovesUseCase {

    ResultadoPaginado<Move> handle(ListarMovesQuery query);
}
