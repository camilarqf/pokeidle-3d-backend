package br.com.pokeidle3d.application.usecases.listarmovesportype;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListarMovesPorTypeHandler implements ListarMovesPorTypeUseCase {

    private final MoveRepository moveRepository;

    public ListarMovesPorTypeHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoPaginado<Move> handle(ListarMovesPorTypeQuery query) {
        return moveRepository.listarPorType(query.type(), query.pagina(), query.tamanho());
    }
}
