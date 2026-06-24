package br.com.pokeidle3d.application.usecases.buscarmoveporname;

import br.com.pokeidle3d.domain.entities.Move;

public interface BuscarMovePorNameUseCase {

    Move handle(BuscarMovePorNameQuery query);
}
