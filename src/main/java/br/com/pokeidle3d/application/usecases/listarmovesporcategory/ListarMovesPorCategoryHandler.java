package br.com.pokeidle3d.application.usecases.listarmovesporcategory;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListarMovesPorCategoryHandler implements ListarMovesPorCategoryUseCase {

    private final MoveRepository moveRepository;

    public ListarMovesPorCategoryHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoPaginado<Move> handle(ListarMovesPorCategoryQuery query) {
        return moveRepository.listarPorCategory(query.category(), query.pagina(), query.tamanho());
    }
}
