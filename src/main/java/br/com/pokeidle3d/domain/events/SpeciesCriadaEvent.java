package br.com.pokeidle3d.domain.events;

import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpeciesCriadaEvent extends DomainEvent {

    private final Integer pokedexNumber;
    private final String name;
    private final PokemonType primaryType;
    private final PokemonType secondaryType;

    private SpeciesCriadaEvent(
            CorrelationKey correlationKey,
            Integer pokedexNumber,
            String name,
            PokemonType primaryType,
            PokemonType secondaryType
    ) {
        super("Species", correlationKey);
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
    }

    public static SpeciesCriadaEvent criar(
            CorrelationKey correlationKey,
            Integer pokedexNumber,
            String name,
            PokemonType primaryType,
            PokemonType secondaryType
    ) {
        return new SpeciesCriadaEvent(correlationKey, pokedexNumber, name, primaryType, secondaryType);
    }

    public Integer pokedexNumber() {
        return pokedexNumber;
    }

    public String name() {
        return name;
    }

    public PokemonType primaryType() {
        return primaryType;
    }

    public PokemonType secondaryType() {
        return secondaryType;
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
        dados.put("pokedexNumber", pokedexNumber);
        dados.put("name", name);
        dados.put("primaryType", primaryType);
        dados.put("secondaryType", secondaryType);
        return dados;
    }
}
