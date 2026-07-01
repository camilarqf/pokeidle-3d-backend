package br.com.pokeidle3d.application.usecases.createmove;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.exceptions.DuplicateMoveException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateMoveHandler implements CreateMoveUseCase {

    private final MoveRepository moveRepository;

    public CreateMoveHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional
    public Move handle(CreateMoveCommand command) {
        if (moveRepository.existePorName(command.name())) {
            throw new DuplicateMoveException("Ja existe movimento com este nome");
        }

        Move move = Move.criar(
                command.name(),
                command.type(),
                command.power(),
                command.accuracy(),
                command.category(),
                command.pp()
        );

        return moveRepository.salvar(move);
    }
}
