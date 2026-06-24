package br.com.pokeidle3d.application.usecases.listarmovesporcategory;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public record ListarMovesPorCategoryQuery(MoveCategory category, int pagina, int tamanho) implements Query<ResultadoPaginado<Move>> {
}
