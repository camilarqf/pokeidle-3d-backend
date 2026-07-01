package br.com.pokeidle3d.application.usecases.findspeciesbypokedexnumber;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Species;

public record FindSpeciesByPokedexNumberQuery(Integer pokedexNumber) implements Query<Species> {
}
