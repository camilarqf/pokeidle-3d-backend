package br.com.pokeidle3d.infra.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "TB_EVENTO")
public class EventJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 500)
    private String fullName;

    @Column(name = "aggregate_type", nullable = false, length = 120)
    private String aggregateType;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "user_name", length = 120)
    private String userName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected EventJpaEntity() {
    }

    public EventJpaEntity(Long id, String fullName, String aggregateType, String payload, String userName, Instant createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.aggregateType = aggregateType;
        this.payload = payload;
        this.userName = userName;
        this.createdAt = createdAt;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getPayload() {
        return payload;
    }

    public String getUserName() {
        return userName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
