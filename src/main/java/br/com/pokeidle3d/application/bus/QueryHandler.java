package br.com.pokeidle3d.application.bus;

public interface QueryHandler<Q extends Query<R>, R> {

    R handle(Q query);
}
