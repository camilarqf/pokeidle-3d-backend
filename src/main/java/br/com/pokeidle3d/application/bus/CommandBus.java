package br.com.pokeidle3d.application.bus;

public interface CommandBus {

    <R> R dispatch(Command<R> command);
}
