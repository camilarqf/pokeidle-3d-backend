package br.com.pokeidle3d.application.bus;

public interface QueryBus {

    <R> R dispatch(Query<R> query);
}
