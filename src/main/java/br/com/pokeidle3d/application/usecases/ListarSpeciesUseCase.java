package br.com.pokeidle3d.application.usecases;

import br.com.pokeidle3d.application.queries.ListarSpeciesQuery;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public interface ListarSpeciesUseCase {

    ResultadoPaginado<Species> handle(ListarSpeciesQuery query);
}
