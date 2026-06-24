package br.com.pokeidle3d.infra.persistence;

import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "especies")
public class SpeciesJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pokedex_number", nullable = false, unique = true)
    private Integer pokedexNumber;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_type", nullable = false, length = 30)
    private PokemonType primaryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "secondary_type", length = 30)
    private PokemonType secondaryType;

    @Column(name = "base_hp", nullable = false)
    private Integer baseHp;

    @Column(name = "base_attack", nullable = false)
    private Integer baseAttack;

    @Column(name = "base_defense", nullable = false)
    private Integer baseDefense;

    @Column(name = "base_special_attack", nullable = false)
    private Integer baseSpecialAttack;

    @Column(name = "base_special_defense", nullable = false)
    private Integer baseSpecialDefense;

    @Column(name = "base_speed", nullable = false)
    private Integer baseSpeed;

    @Column(name = "sprite_ref", length = 1000)
    private String spriteRef;

    @Column(name = "model3d_ref", length = 1000)
    private String model3dRef;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected SpeciesJpaEntity() {
    }

    public SpeciesJpaEntity(
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
        this.id = id;
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpecialAttack = baseSpecialAttack;
        this.baseSpecialDefense = baseSpecialDefense;
        this.baseSpeed = baseSpeed;
        this.spriteRef = spriteRef;
        this.model3dRef = model3dRef;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    void prePersist() {
        Instant agora = Instant.now();
        createdAt = agora;
        updatedAt = agora;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
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
}
