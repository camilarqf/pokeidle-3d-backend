package br.com.pokeidle3d.domain.events;

import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;

import java.util.LinkedHashMap;
import java.util.Map;

public class MoveRemovedFromSpeciesMovesetEvent extends DomainEvent {

    private final Long speciesId;
    private final Long moveId;
    private final MoveLearnMethod learnMethod;
    private final Integer levelLearnedAt;

    private MoveRemovedFromSpeciesMovesetEvent(
            CorrelationKey correlationKey,
            Long speciesId,
            Long moveId,
            MoveLearnMethod learnMethod,
            Integer levelLearnedAt
    ) {
        super("SpeciesMove", correlationKey);
        this.speciesId = speciesId;
        this.moveId = moveId;
        this.learnMethod = learnMethod;
        this.levelLearnedAt = levelLearnedAt;
    }

    public static MoveRemovedFromSpeciesMovesetEvent criar(
            CorrelationKey correlationKey,
            Long speciesId,
            Long moveId,
            MoveLearnMethod learnMethod,
            Integer levelLearnedAt
    ) {
        return new MoveRemovedFromSpeciesMovesetEvent(correlationKey, speciesId, moveId, learnMethod, levelLearnedAt);
    }

    public Long speciesId() {
        return speciesId;
    }

    public Long moveId() {
        return moveId;
    }

    public MoveLearnMethod learnMethod() {
        return learnMethod;
    }

    public Integer levelLearnedAt() {
        return levelLearnedAt;
    }

    @Override
    public Map<String, Object> paraMapa() {
        Map<String, Object> dados = new LinkedHashMap<>();
        dados.put("eventId", eventId());
        dados.put("occurredAt", occurredAt());
        dados.put("correlationKey", correlationKey().value());
        dados.put("aggregateType", aggregateType());
        dados.put("aggregateId", aggregateId());
        dados.put("userName", userName());
        dados.put("ipRequest", ipRequest());
        dados.put("userAgent", userAgent());
        dados.put("perfil", perfil());
        dados.put("unidade", unidade());
        dados.put("speciesId", speciesId);
        dados.put("moveId", moveId);
        dados.put("learnMethod", learnMethod);
        dados.put("levelLearnedAt", levelLearnedAt);
        return dados;
    }
}
