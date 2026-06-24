package br.com.pokeidle3d.application.usecases.buscarspeciesporid;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Species;

public record BuscarSpeciesPorIdQuery(Long id) implements Query<Species> {
}
