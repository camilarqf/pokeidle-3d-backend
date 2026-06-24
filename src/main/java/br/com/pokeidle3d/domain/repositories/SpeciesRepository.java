package br.com.pokeidle3d.domain.repositories;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

import java.util.Optional;

public interface SpeciesRepository {

    Species salvar(Species species);

    Optional<Species> buscarPorId(Long id);

    Optional<Species> buscarPorPokedexNumber(Integer pokedexNumber);

    ResultadoPaginado<Species> listar(int pagina, int tamanho);

    boolean existePorPokedexNumber(Integer pokedexNumber);

    boolean existePorName(String name);
}
