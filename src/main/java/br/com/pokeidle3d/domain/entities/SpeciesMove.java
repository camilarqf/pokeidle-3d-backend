package br.com.pokeidle3d.domain.entities;

import br.com.pokeidle3d.domain.events.MoveAdicionadoAoMovesetSpeciesEvent;
import br.com.pokeidle3d.domain.events.MoveRemovidoDoMovesetSpeciesEvent;
import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import br.com.pokeidle3d.domain.valueobjects.LevelLearnedAt;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;

import java.time.Instant;
import java.util.Objects;

public class SpeciesMove extends AggregateEventManager {

    private final Long id;
    private final Long speciesId;
    private final Long moveId;
    private final MoveLearnMethod learnMethod;
    private final Integer levelLearnedAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    private SpeciesMove(
            Long id,
            Long speciesId,
            Long moveId,
            MoveLearnMethod learnMethod,
            Integer levelLearnedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        validar(speciesId, moveId, learnMethod, levelLearnedAt);
        this.id = id;
        this.speciesId = speciesId;
        this.moveId = moveId;
        this.learnMethod = learnMethod;
        this.levelLearnedAt = normalizarLevel(learnMethod, levelLearnedAt);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SpeciesMove adicionar(
            Long speciesId,
            Long moveId,
            MoveLearnMethod learnMethod,
            Integer levelLearnedAt,
            CorrelationKey correlationKey
    ) {
        SpeciesMove speciesMove = new SpeciesMove(null, speciesId, moveId, learnMethod, levelLearnedAt, null, null);
        speciesMove.registrarEvento(MoveAdicionadoAoMovesetSpeciesEvent.criar(
                correlationKey,
                speciesMove.getSpeciesId(),
                speciesMove.getMoveId(),
                speciesMove.getLearnMethod(),
                speciesMove.getLevelLearnedAt()
        ));
        return speciesMove;
    }

    public static SpeciesMove restaurar(
            Long id,
            Long speciesId,
            Long moveId,
            MoveLearnMethod learnMethod,
            Integer levelLearnedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new SpeciesMove(id, speciesId, moveId, learnMethod, levelLearnedAt, createdAt, updatedAt);
    }

    public void registrarRemocao(CorrelationKey correlationKey) {
        registrarEvento(MoveRemovidoDoMovesetSpeciesEvent.criar(
                correlationKey,
                speciesId,
                moveId,
                learnMethod,
                levelLearnedAt
        ));
    }

    private static void validar(Long speciesId, Long moveId, MoveLearnMethod learnMethod, Integer levelLearnedAt) {
        if (speciesId == null || speciesId <= 0) {
            throw new ValidacaoDominioException("SpeciesId deve ser positivo");
        }
        if (moveId == null || moveId <= 0) {
            throw new ValidacaoDominioException("MoveId deve ser positivo");
        }
        if (learnMethod == null) {
            throw new ValidacaoDominioException("Metodo de aprendizado e obrigatorio");
        }
        if (learnMethod == MoveLearnMethod.LEVEL_UP) {
            LevelLearnedAt.obrigatorioParaLevelUp(learnMethod, levelLearnedAt);
        }
        if (learnMethod != MoveLearnMethod.LEVEL_UP && levelLearnedAt != null && levelLearnedAt <= 0) {
            throw new ValidacaoDominioException("LevelLearnedAt deve ser maior que zero");
        }
    }

    private static Integer normalizarLevel(MoveLearnMethod learnMethod, Integer levelLearnedAt) {
        if (learnMethod == MoveLearnMethod.LEVEL_UP) {
            return levelLearnedAt;
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public Long getMoveId() {
        return moveId;
    }

    public MoveLearnMethod getLearnMethod() {
        return learnMethod;
    }

    public Integer getLevelLearnedAt() {
        return levelLearnedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpeciesMove that)) {
            return false;
        }
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
