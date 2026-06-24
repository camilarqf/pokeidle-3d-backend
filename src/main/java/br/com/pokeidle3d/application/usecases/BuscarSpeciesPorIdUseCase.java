package br.com.pokeidle3d.application.usecases;

import br.com.pokeidle3d.application.queries.BuscarSpeciesPorIdQuery;
import br.com.pokeidle3d.domain.entities.Species;

public interface BuscarSpeciesPorIdUseCase {

    Species handle(BuscarSpeciesPorIdQuery query);
}
