package br.com.pokeidle3d.domain.valueobjects;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;

import java.util.UUID;

public record CorrelationKey(String value) {

    public CorrelationKey {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("CorrelationKey e obrigatoria");
        }
        value = value.trim();
    }

    public static CorrelationKey gerar() {
        return new CorrelationKey(UUID.randomUUID().toString());
    }

    public static CorrelationKey de(String value) {
        if (value == null || value.isBlank()) {
            return gerar();
        }
        return new CorrelationKey(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
