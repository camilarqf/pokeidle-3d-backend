package br.com.pokeidle3d.application.usecases.listarmoves;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListarMovesHandler implements ListarMovesUseCase {

    private final MoveRepository moveRepository;

    public ListarMovesHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoPaginado<Move> handle(ListarMovesQuery query) {
        return moveRepository.listar(query.pagina(), query.tamanho());
    }
}
