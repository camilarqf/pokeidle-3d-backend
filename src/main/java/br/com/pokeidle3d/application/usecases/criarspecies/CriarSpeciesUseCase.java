package br.com.pokeidle3d.application.usecases.criarspecies;

import br.com.pokeidle3d.application.bus.CommandHandler;
import br.com.pokeidle3d.domain.entities.Species;

public interface CriarSpeciesUseCase extends CommandHandler<CriarSpeciesCommand, Species> {
}
