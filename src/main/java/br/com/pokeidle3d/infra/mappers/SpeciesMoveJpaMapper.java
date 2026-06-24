package br.com.pokeidle3d.infra.mappers;

import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.infra.persistence.SpeciesMoveJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SpeciesMoveJpaMapper {

    public SpeciesMoveJpaEntity paraJpa(SpeciesMove speciesMove) {
        return new SpeciesMoveJpaEntity(
                speciesMove.getId(),
                speciesMove.getSpeciesId(),
                speciesMove.getMoveId(),
                speciesMove.getLearnMethod(),
                speciesMove.getLevelLearnedAt(),
                speciesMove.getCreatedAt(),
                speciesMove.getUpdatedAt()
        );
    }

    public SpeciesMove paraDominio(SpeciesMoveJpaEntity entity) {
        return SpeciesMove.restaurar(
                entity.getId(),
                entity.getSpeciesId(),
                entity.getMoveId(),
                entity.getLearnMethod(),
                entity.getLevelLearnedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
