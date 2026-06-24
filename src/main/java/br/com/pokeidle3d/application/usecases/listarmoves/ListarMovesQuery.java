package br.com.pokeidle3d.application.usecases.listarmoves;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public record ListarMovesQuery(int pagina, int tamanho) implements Query<ResultadoPaginado<Move>> {
}
