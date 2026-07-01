package br.com.pokeidle3d.application.usecases.listspeciespormove;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.entities.SpeciesMove;

public record SpeciesByMoveItem(SpeciesMove speciesMove, Species species) {
}
