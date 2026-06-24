package br.com.pokeidle3d.application.usecases.listarmovesportype;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public record ListarMovesPorTypeQuery(PokemonType type, int pagina, int tamanho) implements Query<ResultadoPaginado<Move>> {
}
