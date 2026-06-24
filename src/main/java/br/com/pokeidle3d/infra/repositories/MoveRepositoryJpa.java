package br.com.pokeidle3d.infra.repositories;

import br.com.pokeidle3d.domain.entities.Move;
import br.com.pokeidle3d.domain.exceptions.MoveDuplicadoException;
import br.com.pokeidle3d.domain.repositories.MoveRepository;
import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;
import br.com.pokeidle3d.infra.mappers.MoveJpaMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MoveRepositoryJpa implements MoveRepository {

    private final SpringDataMoveJpaRepository repository;
    private final MoveJpaMapper mapper;

    public MoveRepositoryJpa(SpringDataMoveJpaRepository repository, MoveJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Move salvar(Move move) {
        try {
            return mapper.paraDominio(repository.save(mapper.paraJpa(move)));
        } catch (DataIntegrityViolationException exception) {
            throw new MoveDuplicadoException("Ja existe movimento com este nome");
        }
    }

    @Override
    public Optional<Move> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::paraDominio);
    }

    @Override
    public Optional<Move> buscarPorName(String name) {
        return repository.findByNameIgnoreCase(name).map(mapper::paraDominio);
    }

    @Override
    public ResultadoPaginado<Move> listar(int pagina, int tamanho) {
        Page<Move> page = repository.findAll(pageRequest(pagina, tamanho)).map(mapper::paraDominio);
        return paraResultadoPaginado(page);
    }

    @Override
    public ResultadoPaginado<Move> listarPorType(PokemonType type, int pagina, int tamanho) {
        Page<Move> page = repository.findByType(type, pageRequest(pagina, tamanho)).map(mapper::paraDominio);
        return paraResultadoPaginado(page);
    }

    @Override
    public ResultadoPaginado<Move> listarPorCategory(MoveCategory category, int pagina, int tamanho) {
        Page<Move> page = repository.findByCategory(category, pageRequest(pagina, tamanho)).map(mapper::paraDominio);
        return paraResultadoPaginado(page);
    }

    @Override
    public boolean existePorName(String name) {
        return name != null && repository.existsByNameIgnoreCase(name.trim());
    }

    private PageRequest pageRequest(int pagina, int tamanho) {
        return PageRequest.of(pagina, tamanho, Sort.by("name").ascending());
    }

    private ResultadoPaginado<Move> paraResultadoPaginado(Page<Move> page) {
        return new ResultadoPaginado<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }
}
