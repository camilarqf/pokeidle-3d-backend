package br.com.pokeidle3d.application.usecases.listarmovesportype;

import br.com.pokeidle3d.domain.valueobjects.PokemonType;

public record ListarMovesPorTypeQuery(PokemonType type, int pagina, int tamanho) {
}
