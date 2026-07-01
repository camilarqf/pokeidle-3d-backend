package br.com.pokeidle3d.application.usecases.listmovesetdaspecies;

import br.com.pokeidle3d.application.bus.Query;

import java.util.List;

public record ListSpeciesMovesetQuery(Long speciesId) implements Query<List<SpeciesMovesetItem>> {
}
