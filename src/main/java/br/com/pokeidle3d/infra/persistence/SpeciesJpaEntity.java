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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Entity
@Table(name = "especies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
}
