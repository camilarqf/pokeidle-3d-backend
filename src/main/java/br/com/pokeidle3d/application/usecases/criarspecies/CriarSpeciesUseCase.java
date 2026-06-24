package br.com.pokeidle3d.application.usecases.criarspecies;

import br.com.pokeidle3d.domain.entities.Species;

public interface CriarSpeciesUseCase {

    Species handle(CriarSpeciesCommand command);
}
