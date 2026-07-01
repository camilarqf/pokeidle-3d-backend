package br.com.pokeidle3d.application.usecases.findmovebyname;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.exceptions.MoveNotFoundException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindMoveByNameHandler implements FindMoveByNameUseCase {

    private final MoveRepository moveRepository;

    public FindMoveByNameHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Move handle(FindMoveByNameQuery query) {
        return moveRepository.buscarPorName(query.name())
                .orElseThrow(() -> new MoveNotFoundException("Movimento nao encontrado"));
    }
}
