package br.com.pokeidle3d.application.usecases.listarmovesetdaspecies;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.entities.SpeciesMove;

public record MovesetSpeciesItem(SpeciesMove speciesMove, Move move) {
}
