package br.com.pokeidle3d.application.usecases.buscarmoveporname;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Move;

public record BuscarMovePorNameQuery(String name) implements Query<Move> {
}
