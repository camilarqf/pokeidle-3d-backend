package br.com.pokeidle3d.domain.entities;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;
import br.com.pokeidle3d.domain.events.SpeciesCreatedEvent;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;

import java.time.Instant;
import java.util.Objects;

public class Species extends AggregateEventManager {

    private final Long id;
    private final Integer pokedexNumber;
    private final String name;
    private final PokemonType primaryType;
    private final PokemonType secondaryType;
    private final Integer baseHp;
    private final Integer baseAttack;
    private final Integer baseDefense;
    private final Integer baseSpecialAttack;
    private final Integer baseSpecialDefense;
    private final Integer baseSpeed;
    private final String spriteRef;
    private final String model3dRef;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Species(
            Long id,
            Integer pokedexNumber,
            String name,
            PokemonType primaryType,
            PokemonType secondaryType,
            Integer baseHp,
            Integer baseAttack,
            Integer baseDefense,
            Integer baseSpecialAttack,
            Integer baseSpecialDefense,
            Integer baseSpeed,
            String spriteRef,
            String model3dRef,
            Instant createdAt,
            Instant updatedAt
    ) {
        validar(pokedexNumber, name, primaryType, baseHp, baseAttack, baseDefense, baseSpecialAttack, baseSpecialDefense, baseSpeed);
        this.id = id;
        this.pokedexNumber = pokedexNumber;
        this.name = name.trim().toLowerCase();
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpecialAttack = baseSpecialAttack;
        this.baseSpecialDefense = baseSpecialDefense;
        this.baseSpeed = baseSpeed;
        this.spriteRef = normalizarTextoOpcional(spriteRef);
        this.model3dRef = normalizarTextoOpcional(model3dRef);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Species criar(
            Integer pokedexNumber,
            String name,
            PokemonType primaryType,
            PokemonType secondaryType,
            Integer baseHp,
            Integer baseAttack,
            Integer baseDefense,
            Integer baseSpecialAttack,
            Integer baseSpecialDefense,
            Integer baseSpeed,
            String spriteRef,
            String model3dRef
    ) {
        return criar(
                pokedexNumber,
                name,
                primaryType,
                secondaryType,
                baseHp,
                baseAttack,
                baseDefense,
                baseSpecialAttack,
                baseSpecialDefense,
                baseSpeed,
                spriteRef,
                model3dRef,
                CorrelationKey.gerar()
        );
    }

    public static Species criar(
            Integer pokedexNumber,
            String name,
            PokemonType primaryType,
            PokemonType secondaryType,
            Integer baseHp,
            Integer baseAttack,
            Integer baseDefense,
            Integer baseSpecialAttack,
            Integer baseSpecialDefense,
            Integer baseSpeed,
            String spriteRef,
            String model3dRef,
            CorrelationKey correlationKey
    ) {
        Species species = new Species(
                null,
                pokedexNumber,
                name,
                primaryType,
                secondaryType,
                baseHp,
                baseAttack,
                baseDefense,
                baseSpecialAttack,
                baseSpecialDefense,
                baseSpeed,
                spriteRef,
                model3dRef,
                null,
                null
        );
        species.registrarEvento(SpeciesCreatedEvent.criar(
                correlationKey,
                species.getPokedexNumber(),
                species.getName(),
                species.getPrimaryType(),
                species.getSecondaryType()
        ));
        return species;
    }

    public static Species restaurar(
            Long id,
            Integer pokedexNumber,
            String name,
            PokemonType primaryType,
            PokemonType secondaryType,
            Integer baseHp,
            Integer baseAttack,
            Integer baseDefense,
            Integer baseSpecialAttack,
            Integer baseSpecialDefense,
            Integer baseSpeed,
            String spriteRef,
            String model3dRef,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Species(
                id,
                pokedexNumber,
                name,
                primaryType,
                secondaryType,
                baseHp,
                baseAttack,
                baseDefense,
                baseSpecialAttack,
                baseSpecialDefense,
                baseSpeed,
                spriteRef,
                model3dRef,
                createdAt,
                updatedAt
        );
    }

    private static void validar(
            Integer pokedexNumber,
            String name,
            PokemonType primaryType,
            Integer baseHp,
            Integer baseAttack,
            Integer baseDefense,
            Integer baseSpecialAttack,
            Integer baseSpecialDefense,
            Integer baseSpeed
    ) {
        if (pokedexNumber == null || pokedexNumber <= 0) {
            throw new DomainValidationException("Numero da Pokedex deve ser positivo");
        }
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("Nome da especie e obrigatorio");
        }
        if (primaryType == null) {
            throw new DomainValidationException("Tipo primario e obrigatorio");
        }
        validarStat("baseHp", baseHp);
        validarStat("baseAttack", baseAttack);
        validarStat("baseDefense", baseDefense);
        validarStat("baseSpecialAttack", baseSpecialAttack);
        validarStat("baseSpecialDefense", baseSpecialDefense);
        validarStat("baseSpeed", baseSpeed);
    }

    private static void validarStat(String nome, Integer valor) {
        if (valor == null || valor < 0) {
            throw new DomainValidationException(nome + " nao pode ser negativo");
        }
    }

    private static String normalizarTextoOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }

    public Long getId() {
        return id;
    }

    public Integer getPokedexNumber() {
        return pokedexNumber;
    }

    public String getName() {
        return name;
    }

    public PokemonType getPrimaryType() {
        return primaryType;
    }

    public PokemonType getSecondaryType() {
        return secondaryType;
    }

    public Integer getBaseHp() {
        return baseHp;
    }

    public Integer getBaseAttack() {
        return baseAttack;
    }

    public Integer getBaseDefense() {
        return baseDefense;
    }

    public Integer getBaseSpecialAttack() {
        return baseSpecialAttack;
    }

    public Integer getBaseSpecialDefense() {
        return baseSpecialDefense;
    }

    public Integer getBaseSpeed() {
        return baseSpeed;
    }

    public String getSpriteRef() {
        return spriteRef;
    }

    public String getModel3dRef() {
        return model3dRef;
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
        if (!(o instanceof Species species)) {
            return false;
        }
        return id != null && Objects.equals(id, species.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
