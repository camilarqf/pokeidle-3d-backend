package br.com.pokeidle3d.application.usecases;

import br.com.pokeidle3d.application.queries.BuscarSpeciesPorPokedexNumberQuery;
import br.com.pokeidle3d.domain.entities.Species;

public interface BuscarSpeciesPorPokedexNumberUseCase {

    Species handle(BuscarSpeciesPorPokedexNumberQuery query);
}
