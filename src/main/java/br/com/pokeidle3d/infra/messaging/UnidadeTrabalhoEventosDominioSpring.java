package br.com.pokeidle3d.infra.messaging;

import br.com.pokeidle3d.application.events.PublicadorEventosDominio;
import br.com.pokeidle3d.application.events.UnidadeTrabalhoEventosDominio;
import br.com.pokeidle3d.domain.entities.IAggregate;
import br.com.pokeidle3d.domain.events.DomainEvent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Component
public class UnidadeTrabalhoEventosDominioSpring implements UnidadeTrabalhoEventosDominio {

    private final PublicadorEventosDominio publicadorEventosDominio;
    private final ThreadLocal<List<AggregateComId>> aggregates = ThreadLocal.withInitial(ArrayList::new);

    public UnidadeTrabalhoEventosDominioSpring(PublicadorEventosDominio publicadorEventosDominio) {
        this.publicadorEventosDominio = publicadorEventosDominio;
    }

    @Override
    public void registrar(IAggregate aggregate, Object aggregateId) {
        if (aggregate.events().isEmpty()) {
            return;
        }
        aggregates.get().add(new AggregateComId(aggregate, aggregateId));
    }

    @Override
    public void publicarEventosPendentes() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publicarELimpar();
                }

                @Override
                public void afterCompletion(int status) {
                    aggregates.remove();
                }
            });
            return;
        }

        publicarELimpar();
        aggregates.remove();
    }

    private void publicarELimpar() {
        List<AggregateComId> pendentes = new ArrayList<>(aggregates.get());
        if (pendentes.isEmpty()) {
            return;
        }

        List<DomainEvent> eventos = new ArrayList<>();
        for (AggregateComId item : pendentes) {
            for (DomainEvent evento : item.aggregate().events()) {
                enrichDomainEvent(evento, item.aggregateId());
                eventos.add(evento);
            }
        }

        publicadorEventosDominio.publicar(eventos);
        pendentes.forEach(item -> item.aggregate().clearEvents());
    }

    private void enrichDomainEvent(DomainEvent evento, Object aggregateId) {
        evento.definirAggregateId(String.valueOf(aggregateId));
        evento.definirUserName("anonymous");
        evento.definirPerfil(null);
        evento.definirUnidade(null);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        evento.definirIpRequest(request.getRemoteAddr());
        evento.definirUserAgent(request.getHeader("User-Agent"));
        evento.definirUserName(request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous");
    }

    private record AggregateComId(IAggregate aggregate, Object aggregateId) {
    }
}
