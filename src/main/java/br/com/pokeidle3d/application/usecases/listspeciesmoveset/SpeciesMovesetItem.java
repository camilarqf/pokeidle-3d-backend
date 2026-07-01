package br.com.pokeidle3d.application.usecases.listmovesetdaspecies;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.entities.SpeciesMove;

public record SpeciesMovesetItem(SpeciesMove speciesMove, Move move) {
}
