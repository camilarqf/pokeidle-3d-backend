package br.com.pokeidle3d.api.context;

import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import org.springframework.stereotype.Component;

@Component
public class CorrelationKeyContext {

    public static final String HEADER_NAME = "X-Correlation-Key";
    public static final String REQUEST_ATTRIBUTE_NAME = "correlationKey";

    private final ThreadLocal<CorrelationKey> atual = new ThreadLocal<>();

    public void definir(CorrelationKey correlationKey) {
        atual.set(correlationKey);
    }

    public CorrelationKey atual() {
        CorrelationKey correlationKey = atual.get();
        if (correlationKey == null) {
            correlationKey = CorrelationKey.gerar();
            definir(correlationKey);
        }
        return correlationKey;
    }

    public void limpar() {
        atual.remove();
    }
}
