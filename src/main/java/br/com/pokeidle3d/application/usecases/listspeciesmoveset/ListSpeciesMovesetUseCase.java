package br.com.pokeidle3d.application.usecases.listmovesetdaspecies;

import br.com.pokeidle3d.application.bus.QueryHandler;

import java.util.List;

public interface ListSpeciesMovesetUseCase extends QueryHandler<ListSpeciesMovesetQuery, List<SpeciesMovesetItem>> {
}
