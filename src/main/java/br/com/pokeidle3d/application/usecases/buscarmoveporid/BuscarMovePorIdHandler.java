package br.com.pokeidle3d.application.usecases.buscarmoveporid;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.exceptions.MoveNaoEncontradoException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuscarMovePorIdHandler implements BuscarMovePorIdUseCase {

    private final MoveRepository moveRepository;

    public BuscarMovePorIdHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Move handle(BuscarMovePorIdQuery query) {
        return moveRepository.buscarPorId(query.id())
                .orElseThrow(() -> new MoveNaoEncontradoException("Movimento nao encontrado"));
    }
}
