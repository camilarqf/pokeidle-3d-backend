package br.com.pokeidle3d.application.usecases.listarmovesetdaspecies;

import br.com.pokeidle3d.application.bus.QueryHandler;

import java.util.List;

public interface ListarMovesetDaSpeciesUseCase extends QueryHandler<ListarMovesetDaSpeciesQuery, List<MovesetSpeciesItem>> {
}
