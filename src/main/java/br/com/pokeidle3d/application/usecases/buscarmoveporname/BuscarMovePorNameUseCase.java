package br.com.pokeidle3d.application.usecases.buscarmoveporname;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Move;

public interface BuscarMovePorNameUseCase extends QueryHandler<BuscarMovePorNameQuery, Move> {
}
