package br.com.pokeidle3d.application.usecases.listarmovesetdaspecies;

import br.com.pokeidle3d.application.bus.Query;

import java.util.List;

public record ListarMovesetDaSpeciesQuery(Long speciesId) implements Query<List<MovesetSpeciesItem>> {
}
