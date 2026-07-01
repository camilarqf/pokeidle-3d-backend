package br.com.pokeidle3d.application.usecases.listspecies;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;

public interface ListSpeciesUseCase extends QueryHandler<ListSpeciesQuery, PaginatedResult<Species>> {
}
