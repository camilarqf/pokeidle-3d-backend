package br.com.pokeidle3d.api.mappers;

import br.com.pokeidle3d.api.contracts.CreateMoveRequest;
import br.com.pokeidle3d.api.contracts.MoveResponse;
import br.com.pokeidle3d.api.contracts.PageResponse;
import br.com.pokeidle3d.application.usecases.createmove.CreateMoveCommand;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;
import org.springframework.stereotype.Component;

@Component
public class MoveApiMapper {

    public CreateMoveCommand paraCommand(CreateMoveRequest request) {
        return new CreateMoveCommand(
                request.name(),
                request.type(),
                request.power(),
                request.accuracy(),
                request.category(),
                request.pp()
        );
    }

    public MoveResponse paraResponse(Move move) {
        return new MoveResponse(
                move.getId(),
                move.getName(),
                move.getType(),
                move.getPower(),
                move.getAccuracy(),
                move.getCategory(),
                move.getPp(),
                move.getCreatedAt(),
                move.getUpdatedAt()
        );
    }

    public PageResponse<MoveResponse> paraPageResponse(PaginatedResult<Move> pagina) {
        return new PageResponse<>(
                pagina.itens().stream().map(this::paraResponse).toList(),
                pagina.totalItens(),
                pagina.totalPaginas(),
                pagina.pagina(),
                pagina.tamanho()
        );
    }
}
