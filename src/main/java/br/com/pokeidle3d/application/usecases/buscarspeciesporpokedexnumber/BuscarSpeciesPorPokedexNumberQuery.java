package br.com.pokeidle3d.application.usecases.buscarspeciesporpokedexnumber;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Species;

public record BuscarSpeciesPorPokedexNumberQuery(Integer pokedexNumber) implements Query<Species> {
}
