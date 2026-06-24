package br.com.pokeidle3d.application.usecases;

import br.com.pokeidle3d.application.commands.CriarSpeciesCommand;
import br.com.pokeidle3d.domain.entities.Species;

public interface CriarSpeciesUseCase {

    Species handle(CriarSpeciesCommand command);
}
