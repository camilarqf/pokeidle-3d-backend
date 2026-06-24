package br.com.pokeidle3d.infra.mappers;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.infra.persistence.SpeciesJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SpeciesJpaMapper {

    public SpeciesJpaEntity paraJpa(Species species) {
        return new SpeciesJpaEntity(
                species.getId(),
                species.getPokedexNumber(),
                species.getName(),
                species.getPrimaryType(),
                species.getSecondaryType(),
                species.getBaseHp(),
                species.getBaseAttack(),
                species.getBaseDefense(),
                species.getBaseSpecialAttack(),
                species.getBaseSpecialDefense(),
                species.getBaseSpeed(),
                species.getSpriteRef(),
                species.getModel3dRef(),
                species.getCreatedAt(),
                species.getUpdatedAt()
        );
    }

    public Species paraDominio(SpeciesJpaEntity entity) {
        return Species.restaurar(
                entity.getId(),
                entity.getPokedexNumber(),
                entity.getName(),
                entity.getPrimaryType(),
                entity.getSecondaryType(),
                entity.getBaseHp(),
                entity.getBaseAttack(),
                entity.getBaseDefense(),
                entity.getBaseSpecialAttack(),
                entity.getBaseSpecialDefense(),
                entity.getBaseSpeed(),
                entity.getSpriteRef(),
                entity.getModel3dRef(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
