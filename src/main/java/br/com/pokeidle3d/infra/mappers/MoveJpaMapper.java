package br.com.pokeidle3d.infra.mappers;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.infra.persistence.MoveJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class MoveJpaMapper {

    public MoveJpaEntity paraJpa(Move move) {
        return new MoveJpaEntity(
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

    public Move paraDominio(MoveJpaEntity entity) {
        return Move.restaurar(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getPower(),
                entity.getAccuracy(),
                entity.getCategory(),
                entity.getPp(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
