package br.com.pokeidle3d.application.usecases.listmoves;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListMovesHandler implements ListMovesUseCase {

    private final MoveRepository moveRepository;

    public ListMovesHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResult<Move> handle(ListMovesQuery query) {
        return moveRepository.listar(query.pagina(), query.tamanho());
    }
}
