package br.com.pokeidle3d.application.usecases.listspeciespormove;

import br.com.pokeidle3d.application.bus.QueryHandler;

import java.util.List;

public interface ListSpeciesByMoveUseCase extends QueryHandler<ListSpeciesByMoveQuery, List<SpeciesByMoveItem>> {
}
