package br.com.pokeidle3d.domain.eventstore.entities;

import java.time.Instant;

public class Event {

    private final Long id;
    private final String fullName;
    private final String aggregateType;
    private final String payload;
    private final String userName;
    private final Instant createdAt;

    public Event(Long id, String fullName, String aggregateType, String payload, String userName, Instant createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.aggregateType = aggregateType;
        this.payload = payload;
        this.userName = userName;
        this.createdAt = createdAt;
    }

    public static Event criar(String fullName, String aggregateType, String payload, String userName) {
        return new Event(null, fullName, aggregateType, payload, userName, Instant.now());
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
