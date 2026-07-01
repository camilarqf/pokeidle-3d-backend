package br.com.pokeidle3d.api.mappers;

import br.com.pokeidle3d.api.contracts.AddMoveToSpeciesMovesetRequest;
import br.com.pokeidle3d.api.contracts.SpeciesMovesetResponse;
import br.com.pokeidle3d.api.contracts.SpeciesMoveResponse;
import br.com.pokeidle3d.api.contracts.SpeciesByMoveResponse;
import br.com.pokeidle3d.application.usecases.addmovetospeciesmoveset.AddMoveToSpeciesMovesetCommand;
import br.com.pokeidle3d.application.usecases.listmovesetdaspecies.SpeciesMovesetItem;
import br.com.pokeidle3d.application.usecases.listspeciespormove.SpeciesByMoveItem;
import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;
import org.springframework.stereotype.Component;

@Component
public class SpeciesMoveApiMapper {

    private final MoveApiMapper moveApiMapper;
    private final SpeciesApiMapper speciesApiMapper;

    public SpeciesMoveApiMapper(MoveApiMapper moveApiMapper, SpeciesApiMapper speciesApiMapper) {
        this.moveApiMapper = moveApiMapper;
        this.speciesApiMapper = speciesApiMapper;
    }

    public AddMoveToSpeciesMovesetCommand paraCommand(
            Long speciesId,
            AddMoveToSpeciesMovesetRequest request,
            CorrelationKey correlationKey
    ) {
        return new AddMoveToSpeciesMovesetCommand(
                speciesId,
                request.moveId(),
                request.learnMethod(),
                normalizarLevel(request.learnMethod(), request.levelLearnedAt()),
                correlationKey
        );
    }

    public SpeciesMoveResponse paraResponse(SpeciesMove speciesMove) {
        return new SpeciesMoveResponse(
                speciesMove.getId(),
                speciesMove.getSpeciesId(),
                speciesMove.getMoveId(),
                speciesMove.getLearnMethod(),
                speciesMove.getLevelLearnedAt(),
                speciesMove.getCreatedAt(),
                speciesMove.getUpdatedAt()
        );
    }

    public SpeciesMovesetResponse paraMovesetResponse(SpeciesMovesetItem item) {
        SpeciesMove speciesMove = item.speciesMove();
        return new SpeciesMovesetResponse(
                speciesMove.getId(),
                speciesMove.getSpeciesId(),
                moveApiMapper.paraResponse(item.move()),
                speciesMove.getLearnMethod(),
                speciesMove.getLevelLearnedAt(),
                speciesMove.getCreatedAt(),
                speciesMove.getUpdatedAt()
        );
    }

    public SpeciesByMoveResponse paraSpeciesByMoveResponse(SpeciesByMoveItem item) {
        SpeciesMove speciesMove = item.speciesMove();
        return new SpeciesByMoveResponse(
                speciesMove.getId(),
                speciesMove.getMoveId(),
                speciesApiMapper.paraResponse(item.species()),
                speciesMove.getLearnMethod(),
                speciesMove.getLevelLearnedAt(),
                speciesMove.getCreatedAt(),
                speciesMove.getUpdatedAt()
        );
    }

    private Integer normalizarLevel(MoveLearnMethod learnMethod, Integer levelLearnedAt) {
        return learnMethod == MoveLearnMethod.LEVEL_UP ? levelLearnedAt : null;
    }
}
