package br.com.pokeidle3d.application.usecases.buscarspeciesporid;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Species;

public interface BuscarSpeciesPorIdUseCase extends QueryHandler<BuscarSpeciesPorIdQuery, Species> {
}
