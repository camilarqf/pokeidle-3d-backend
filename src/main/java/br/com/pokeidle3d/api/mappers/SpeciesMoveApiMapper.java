package br.com.pokeidle3d.api.mappers;

import br.com.pokeidle3d.api.contracts.AdicionarMoveAoMovesetSpeciesRequest;
import br.com.pokeidle3d.api.contracts.MovesetSpeciesResponse;
import br.com.pokeidle3d.api.contracts.SpeciesMoveResponse;
import br.com.pokeidle3d.api.contracts.SpeciesPorMoveResponse;
import br.com.pokeidle3d.application.usecases.adicionarmoveaomovesetspecies.AdicionarMoveAoMovesetSpeciesCommand;
import br.com.pokeidle3d.application.usecases.listarmovesetdaspecies.MovesetSpeciesItem;
import br.com.pokeidle3d.application.usecases.listarspeciespormove.SpeciesPorMoveItem;
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

    public AdicionarMoveAoMovesetSpeciesCommand paraCommand(
            Long speciesId,
            AdicionarMoveAoMovesetSpeciesRequest request,
            CorrelationKey correlationKey
    ) {
        return new AdicionarMoveAoMovesetSpeciesCommand(
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

    public MovesetSpeciesResponse paraMovesetResponse(MovesetSpeciesItem item) {
        SpeciesMove speciesMove = item.speciesMove();
        return new MovesetSpeciesResponse(
                speciesMove.getId(),
                speciesMove.getSpeciesId(),
                moveApiMapper.paraResponse(item.move()),
                speciesMove.getLearnMethod(),
                speciesMove.getLevelLearnedAt(),
                speciesMove.getCreatedAt(),
                speciesMove.getUpdatedAt()
        );
    }

    public SpeciesPorMoveResponse paraSpeciesPorMoveResponse(SpeciesPorMoveItem item) {
        SpeciesMove speciesMove = item.speciesMove();
        return new SpeciesPorMoveResponse(
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
