package br.com.pokeidle3d.domain.services;

public final class FixedTurnOrderRandomProvider implements TurnOrderRandomProvider {

    private final boolean firstActsFirst;

    public FixedTurnOrderRandomProvider(boolean firstActsFirst) {
        this.firstActsFirst = firstActsFirst;
    }

    @Override
    public boolean firstActsFirst() {
        return firstActsFirst;
    }
}
