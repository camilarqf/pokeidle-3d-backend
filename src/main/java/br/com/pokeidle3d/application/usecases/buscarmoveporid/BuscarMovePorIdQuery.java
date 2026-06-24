package br.com.pokeidle3d.application.usecases.buscarmoveporid;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Move;

public record BuscarMovePorIdQuery(Long id) implements Query<Move> {
}
