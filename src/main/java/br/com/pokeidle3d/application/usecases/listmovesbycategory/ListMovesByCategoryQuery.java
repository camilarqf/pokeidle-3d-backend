package br.com.pokeidle3d.application.usecases.listmovesporcategory;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;

public record ListMovesByCategoryQuery(MoveCategory category, int pagina, int tamanho) implements Query<PaginatedResult<Move>> {
}
