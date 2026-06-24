package br.com.pokeidle3d.api.contracts;

import java.time.Instant;
import java.util.List;

public record ErroResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        String correlationKey,
        List<String> details
) {
}
