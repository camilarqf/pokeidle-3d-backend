package br.com.pokeidle3d.api.filters;

import br.com.pokeidle3d.api.context.CorrelationKeyContext;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CorrelationKeyFilter extends OncePerRequestFilter {

    private final CorrelationKeyContext correlationKeyContext;

    public CorrelationKeyFilter(CorrelationKeyContext correlationKeyContext) {
        this.correlationKeyContext = correlationKeyContext;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        CorrelationKey correlationKey = CorrelationKey.de(request.getHeader(CorrelationKeyContext.HEADER_NAME));
        correlationKeyContext.definir(correlationKey);
        request.setAttribute(CorrelationKeyContext.REQUEST_ATTRIBUTE_NAME, correlationKey.value());
        response.setHeader(CorrelationKeyContext.HEADER_NAME, correlationKey.value());
        MDC.put("correlationKey", correlationKey.value());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("correlationKey");
            correlationKeyContext.limpar();
        }
    }
}
