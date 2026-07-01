package br.com.pokeidle3d.application.usecases.listmovesportype;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;

public record ListMovesByTypeQuery(PokemonType type, int pagina, int tamanho) implements Query<PaginatedResult<Move>> {
}
