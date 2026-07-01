package br.com.pokeidle3d.application.usecases.listspecies;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;

public record ListSpeciesQuery(int pagina, int tamanho) implements Query<PaginatedResult<Species>> {
}
