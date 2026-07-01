package br.com.pokeidle3d.application.usecases.listspeciespormove;

import br.com.pokeidle3d.application.bus.Query;

import java.util.List;

public record ListSpeciesByMoveQuery(Long moveId) implements Query<List<SpeciesByMoveItem>> {
}
