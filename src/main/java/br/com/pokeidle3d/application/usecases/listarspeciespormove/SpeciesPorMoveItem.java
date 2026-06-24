package br.com.pokeidle3d.application.usecases.listarspeciespormove;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.entities.SpeciesMove;

public record SpeciesPorMoveItem(SpeciesMove speciesMove, Species species) {
}
