package br.com.pokeidle3d.application.usecases.createspecies;

import br.com.pokeidle3d.application.bus.CommandHandler;
import br.com.pokeidle3d.domain.entities.Species;

public interface CreateSpeciesUseCase extends CommandHandler<CreateSpeciesCommand, Species> {
}
