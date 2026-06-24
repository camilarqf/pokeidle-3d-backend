package br.com.pokeidle3d.application.usecases.buscarmoveporid;

import br.com.pokeidle3d.domain.entities.Move;

public interface BuscarMovePorIdUseCase {

    Move handle(BuscarMovePorIdQuery query);
}
