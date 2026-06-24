package br.com.pokeidle3d.domain.repositories;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;

import java.util.Optional;

public interface MoveRepository {

    Move salvar(Move move);

    Optional<Move> buscarPorId(Long id);

    Optional<Move> buscarPorName(String name);

    ResultadoPaginado<Move> listar(int pagina, int tamanho);

    ResultadoPaginado<Move> listarPorType(PokemonType type, int pagina, int tamanho);

    ResultadoPaginado<Move> listarPorCategory(MoveCategory category, int pagina, int tamanho);

    boolean existePorName(String name);
}
