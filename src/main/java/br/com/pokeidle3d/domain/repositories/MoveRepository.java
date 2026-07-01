package br.com.pokeidle3d.domain.repositories;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;

import java.util.Optional;

public interface MoveRepository {

    Move salvar(Move move);

    Optional<Move> buscarPorId(Long id);

    Optional<Move> buscarPorName(String name);

    PaginatedResult<Move> listar(int pagina, int tamanho);

    PaginatedResult<Move> listarPorType(PokemonType type, int pagina, int tamanho);

    PaginatedResult<Move> listarPorCategory(MoveCategory category, int pagina, int tamanho);

    boolean existePorName(String name);
}
