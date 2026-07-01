package br.com.pokeidle3d.application.usecases.findspeciesbyid;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Species;

public record FindSpeciesByIdQuery(Long id) implements Query<Species> {
}
