package br.com.pokeidle3d.application.usecases.buscarmoveporname;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.exceptions.MoveNaoEncontradoException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuscarMovePorNameHandler implements BuscarMovePorNameUseCase {

    private final MoveRepository moveRepository;

    public BuscarMovePorNameHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Move handle(BuscarMovePorNameQuery query) {
        return moveRepository.buscarPorName(query.name())
                .orElseThrow(() -> new MoveNaoEncontradoException("Movimento nao encontrado"));
    }
}
