package br.com.pokeidle3d.infra.bus;

import br.com.pokeidle3d.application.bus.Command;
import br.com.pokeidle3d.application.bus.CommandBus;
import br.com.pokeidle3d.application.bus.CommandHandler;
import br.com.pokeidle3d.application.exceptions.HandlerNaoEncontradoException;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpringCommandBus implements CommandBus {

    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new HashMap<>();

    public SpringCommandBus(List<CommandHandler<?, ?>> commandHandlers) {
        commandHandlers.forEach(handler -> handlers.put(resolverCommandType(handler), handler));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R dispatch(Command<R> command) {
        CommandHandler<Command<R>, R> handler = (CommandHandler<Command<R>, R>) handlers.get(command.getClass());
        if (handler == null) {
            throw new HandlerNaoEncontradoException("Command handler nao encontrado para " + command.getClass().getSimpleName());
        }
        return handler.handle(command);
    }

    private Class<?> resolverCommandType(CommandHandler<?, ?> handler) {
        Class<?> commandType = ResolvableType
                .forClass(handler.getClass())
                .as(CommandHandler.class)
                .getGeneric(0)
                .resolve();

        if (commandType == null) {
            throw new HandlerNaoEncontradoException("Nao foi possivel resolver command handler " + handler.getClass().getSimpleName());
        }

        return commandType;
    }
}
