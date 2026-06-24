package br.com.pokeidle3d.application.usecases.listarspeciespormove;

import br.com.pokeidle3d.application.bus.Query;

import java.util.List;

public record ListarSpeciesPorMoveQuery(Long moveId) implements Query<List<SpeciesPorMoveItem>> {
}
