package br.com.pokeidle3d.infra.persistence;

import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(
        name = "species_moves",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_species_moves_species_move_method_level",
                columnNames = {"species_id", "move_id", "learn_method", "level_learned_at"}
        )
)
public class SpeciesMoveJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "species_id", nullable = false)
    private Long speciesId;

    @Column(name = "move_id", nullable = false)
    private Long moveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_species_moves_species"))
    private SpeciesJpaEntity species;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "move_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_species_moves_moves"))
    private MoveJpaEntity move;

    @Enumerated(EnumType.STRING)
    @Column(name = "learn_method", nullable = false, length = 30)
    private MoveLearnMethod learnMethod;

    @Column(name = "level_learned_at")
    private Integer levelLearnedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected SpeciesMoveJpaEntity() {
    }

    public SpeciesMoveJpaEntity(
            Long id,
            Long speciesId,
            Long moveId,
            MoveLearnMethod learnMethod,
            Integer levelLearnedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.speciesId = speciesId;
        this.moveId = moveId;
        this.learnMethod = learnMethod;
        this.levelLearnedAt = levelLearnedAt;
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
}
