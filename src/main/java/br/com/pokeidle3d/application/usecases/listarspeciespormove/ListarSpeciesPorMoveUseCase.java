package br.com.pokeidle3d.application.usecases.listarspeciespormove;

import br.com.pokeidle3d.application.bus.QueryHandler;

import java.util.List;

public interface ListarSpeciesPorMoveUseCase extends QueryHandler<ListarSpeciesPorMoveQuery, List<SpeciesPorMoveItem>> {
}
