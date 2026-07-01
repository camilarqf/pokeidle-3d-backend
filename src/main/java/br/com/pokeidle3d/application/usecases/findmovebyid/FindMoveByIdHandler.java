package br.com.pokeidle3d.application.usecases.findmovebyid;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.exceptions.MoveNotFoundException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindMoveByIdHandler implements FindMoveByIdUseCase {

    private final MoveRepository moveRepository;

    public FindMoveByIdHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Move handle(FindMoveByIdQuery query) {
        return moveRepository.buscarPorId(query.id())
                .orElseThrow(() -> new MoveNotFoundException("Movimento nao encontrado"));
    }
}
