package br.com.pokeidle3d.application.usecases.listarspecies;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public interface ListarSpeciesUseCase extends QueryHandler<ListarSpeciesQuery, ResultadoPaginado<Species>> {
}
