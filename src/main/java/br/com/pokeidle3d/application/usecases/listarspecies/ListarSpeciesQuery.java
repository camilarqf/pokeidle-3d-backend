package br.com.pokeidle3d.application.usecases.listarspecies;

import br.com.pokeidle3d.application.bus.Query;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

public record ListarSpeciesQuery(int pagina, int tamanho) implements Query<ResultadoPaginado<Species>> {
}
