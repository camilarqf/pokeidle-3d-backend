package br.com.pokeidle3d.application.usecases.criarmove;

import br.com.pokeidle3d.application.bus.Command;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;

public record CriarMoveCommand(
        String name,
        PokemonType type,
        Integer power,
        Integer accuracy,
        MoveCategory category,
        Integer pp
) implements Command<Move> {
}
