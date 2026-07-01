package br.com.pokeidle3d.application.usecases.listmovesportype;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListMovesByTypeHandler implements ListMovesByTypeUseCase {

    private final MoveRepository moveRepository;

    public ListMovesByTypeHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResult<Move> handle(ListMovesByTypeQuery query) {
        return moveRepository.listarPorType(query.type(), query.pagina(), query.tamanho());
    }
}
