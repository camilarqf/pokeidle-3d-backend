package br.com.pokeidle3d.api.mappers;

import br.com.pokeidle3d.api.contracts.CriarMoveRequest;
import br.com.pokeidle3d.api.contracts.MoveResponse;
import br.com.pokeidle3d.api.contracts.PaginaResponse;
import br.com.pokeidle3d.application.usecases.criarmove.CriarMoveCommand;
import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;
import org.springframework.stereotype.Component;

@Component
public class MoveApiMapper {

    public CriarMoveCommand paraCommand(CriarMoveRequest request) {
        return new CriarMoveCommand(
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

    public PaginaResponse<MoveResponse> paraPaginaResponse(ResultadoPaginado<Move> pagina) {
        return new PaginaResponse<>(
                pagina.itens().stream().map(this::paraResponse).toList(),
                pagina.totalItens(),
                pagina.totalPaginas(),
                pagina.pagina(),
                pagina.tamanho()
        );
    }
}
