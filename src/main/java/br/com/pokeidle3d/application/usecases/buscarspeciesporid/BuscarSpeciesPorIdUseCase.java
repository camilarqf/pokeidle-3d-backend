package br.com.pokeidle3d.application.usecases.buscarspeciesporid;

import br.com.pokeidle3d.domain.entities.Species;

public interface BuscarSpeciesPorIdUseCase {

    Species handle(BuscarSpeciesPorIdQuery query);
}
