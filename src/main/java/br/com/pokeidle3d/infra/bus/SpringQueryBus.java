package br.com.pokeidle3d.infra.bus;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.application.bus.QueryBus;
import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.application.exceptions.HandlerNotFoundException;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpringQueryBus implements QueryBus {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();

    public SpringQueryBus(List<QueryHandler<?, ?>> queryHandlers) {
        queryHandlers.forEach(handler -> handlers.put(resolverQueryType(handler), handler));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R dispatch(Query<R> query) {
        QueryHandler<Query<R>, R> handler = (QueryHandler<Query<R>, R>) handlers.get(query.getClass());
        if (handler == null) {
            throw new HandlerNotFoundException("Query handler nao encontrado para " + query.getClass().getSimpleName());
        }
        return handler.handle(query);
    }

    private Class<?> resolverQueryType(QueryHandler<?, ?> handler) {
        Class<?> queryType = ResolvableType
                .forClass(handler.getClass())
                .as(QueryHandler.class)
                .getGeneric(0)
                .resolve();

        if (queryType == null) {
            throw new HandlerNotFoundException("Nao foi possivel resolver query handler " + handler.getClass().getSimpleName());
        }

        return queryType;
    }
}
