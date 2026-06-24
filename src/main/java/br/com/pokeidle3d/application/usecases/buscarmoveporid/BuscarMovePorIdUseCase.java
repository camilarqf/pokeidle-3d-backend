package br.com.pokeidle3d.application.usecases.buscarmoveporid;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Move;

public interface BuscarMovePorIdUseCase extends QueryHandler<BuscarMovePorIdQuery, Move> {
}
