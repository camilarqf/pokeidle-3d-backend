package br.com.pokeidle3d.application.usecases.buscarspeciesporpokedexnumber;

import br.com.pokeidle3d.domain.entities.Species;

public interface BuscarSpeciesPorPokedexNumberUseCase {

    Species handle(BuscarSpeciesPorPokedexNumberQuery query);
}
