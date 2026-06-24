package br.com.pokeidle3d.application.usecases.listarmovesporcategory;

import br.com.pokeidle3d.domain.valueobjects.MoveCategory;

public record ListarMovesPorCategoryQuery(MoveCategory category, int pagina, int tamanho) {
}
