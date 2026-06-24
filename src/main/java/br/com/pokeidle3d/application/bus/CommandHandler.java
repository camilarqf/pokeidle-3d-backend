package br.com.pokeidle3d.application.bus;

public interface CommandHandler<C extends Command<R>, R> {

    R handle(C command);
}
