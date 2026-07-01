package br.com.pokeidle3d.application.usecases.findspeciesbypokedexnumber;

import br.com.pokeidle3d.application.bus.QueryHandler;
import br.com.pokeidle3d.domain.entities.Species;

public interface FindSpeciesByPokedexNumberUseCase extends QueryHandler<FindSpeciesByPokedexNumberQuery, Species> {
}
