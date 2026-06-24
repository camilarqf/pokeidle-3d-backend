package br.com.pokeidle3d.infra.repositories;

import br.com.pokeidle3d.application.events.UnidadeTrabalhoEventosDominio;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.exceptions.SpeciesDuplicadaException;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;
import br.com.pokeidle3d.infra.mappers.SpeciesJpaMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpeciesRepositoryJpa implements SpeciesRepository {

    private final SpringDataSpeciesJpaRepository repository;
    private final SpeciesJpaMapper mapper;
    private final UnidadeTrabalhoEventosDominio unidadeTrabalhoEventosDominio;

    public SpeciesRepositoryJpa(
            SpringDataSpeciesJpaRepository repository,
            SpeciesJpaMapper mapper,
            UnidadeTrabalhoEventosDominio unidadeTrabalhoEventosDominio
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.unidadeTrabalhoEventosDominio = unidadeTrabalhoEventosDominio;
    }

    @Override
    public Species salvar(Species species) {
        try {
            Species speciesSalva = mapper.paraDominio(repository.save(mapper.paraJpa(species)));
            unidadeTrabalhoEventosDominio.registrar(species, speciesSalva.getId());
            return speciesSalva;
        } catch (DataIntegrityViolationException exception) {
            throw new SpeciesDuplicadaException("Ja existe especie com este numero da Pokedex ou nome");
        }
    }

    @Override
    public Optional<Species> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::paraDominio);
    }

    @Override
    public Optional<Species> buscarPorPokedexNumber(Integer pokedexNumber) {
        return repository.findByPokedexNumber(pokedexNumber).map(mapper::paraDominio);
    }

    @Override
    public ResultadoPaginado<Species> listar(int pagina, int tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, Sort.by("pokedexNumber").ascending());
        Page<Species> page = repository.findAll(pageRequest).map(mapper::paraDominio);
        return new ResultadoPaginado<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Override
    public boolean existePorPokedexNumber(Integer pokedexNumber) {
        return repository.existsByPokedexNumber(pokedexNumber);
    }

    @Override
    public boolean existePorName(String name) {
        return name != null && repository.existsByNameIgnoreCase(name.trim());
    }
}
