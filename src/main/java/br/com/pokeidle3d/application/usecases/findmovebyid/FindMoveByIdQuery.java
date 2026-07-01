package br.com.pokeidle3d.application.usecases.findmovebyid;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Move;

public record FindMoveByIdQuery(Long id) implements Query<Move> {
}
