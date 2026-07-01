package br.com.pokeidle3d.application.usecases.findmovebyname;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Move;

public record FindMoveByNameQuery(String name) implements Query<Move> {
}
