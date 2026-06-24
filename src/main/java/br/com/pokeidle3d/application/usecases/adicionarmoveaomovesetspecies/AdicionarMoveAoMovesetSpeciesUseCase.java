package br.com.pokeidle3d.application.usecases.adicionarmoveaomovesetspecies;

import br.com.pokeidle3d.application.bus.CommandHandler;
import br.com.pokeidle3d.domain.entities.SpeciesMove;

public interface AdicionarMoveAoMovesetSpeciesUseCase extends CommandHandler<AdicionarMoveAoMovesetSpeciesCommand, SpeciesMove> {
}
