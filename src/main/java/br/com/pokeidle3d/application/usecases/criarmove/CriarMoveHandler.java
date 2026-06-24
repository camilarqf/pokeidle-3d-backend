package br.com.pokeidle3d.application.usecases.criarmove;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.exceptions.MoveDuplicadoException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarMoveHandler implements CriarMoveUseCase {

    private final MoveRepository moveRepository;

    public CriarMoveHandler(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    @Transactional
    public Move handle(CriarMoveCommand command) {
        if (moveRepository.existePorName(command.name())) {
            throw new MoveDuplicadoException("Ja existe movimento com este nome");
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
