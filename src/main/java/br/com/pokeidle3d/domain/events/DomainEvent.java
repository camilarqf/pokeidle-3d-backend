package br.com.pokeidle3d.domain.events;

import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public abstract class DomainEvent {

    private final UUID eventId;
    private final Instant occurredAt;
    private final CorrelationKey correlationKey;
    private final String aggregateType;
    private String aggregateId;
    private String userName;
    private String ipRequest;
    private String userAgent;
    private String perfil;
    private String unidade;

    protected DomainEvent(String aggregateType, CorrelationKey correlationKey) {
        this.eventId = UUID.randomUUID();
        this.occurredAt = Instant.now();
        this.correlationKey = correlationKey;
        this.aggregateType = aggregateType;
    }

    public UUID eventId() {
        return eventId;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    public CorrelationKey correlationKey() {
        return correlationKey;
    }

    public String aggregateType() {
        return aggregateType;
    }

    public String aggregateId() {
        return aggregateId;
    }

    public void definirAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String userName() {
        return userName;
    }

    public void definirUserName(String userName) {
        this.userName = userName;
    }

    public String ipRequest() {
        return ipRequest;
    }

    public void definirIpRequest(String ipRequest) {
        this.ipRequest = ipRequest;
    }

    public String userAgent() {
        return userAgent;
    }

    public void definirUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String perfil() {
        return perfil;
    }

    public void definirPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String unidade() {
        return unidade;
    }

    public void definirUnidade(String unidade) {
        this.unidade = unidade;
    }

    public abstract Map<String, Object> paraMapa();
}
