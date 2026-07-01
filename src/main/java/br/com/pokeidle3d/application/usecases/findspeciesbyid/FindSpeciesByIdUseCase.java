package br.com.pokeidle3d.application.usecases.findspeciesbyid;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Species;

public interface FindSpeciesByIdUseCase extends QueryHandler<FindSpeciesByIdQuery, Species> {
}
