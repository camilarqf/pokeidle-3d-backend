package br.com.pokeidle3d.infra.repositories;

import br.com.pokeidle3d.application.events.DomainEventUnitOfWork;
import br.com.pokeidle3d.domain.entities.SpeciesMove;
import br.com.pokeidle3d.domain.exceptions.DuplicateSpeciesMoveException;
import br.com.pokeidle3d.domain.repositories.SpeciesMoveRepository;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;
import br.com.pokeidle3d.infra.mappers.SpeciesMoveJpaMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SpeciesMoveRepositoryJpa implements SpeciesMoveRepository {

    private final SpringDataSpeciesMoveJpaRepository repository;
    private final SpeciesMoveJpaMapper mapper;
    private final DomainEventUnitOfWork unidadeTrabalhoEventosDominio;

    public SpeciesMoveRepositoryJpa(
            SpringDataSpeciesMoveJpaRepository repository,
            SpeciesMoveJpaMapper mapper,
            DomainEventUnitOfWork unidadeTrabalhoEventosDominio
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.unidadeTrabalhoEventosDominio = unidadeTrabalhoEventosDominio;
    }

    @Override
    public SpeciesMove salvar(SpeciesMove speciesMove) {
        try {
            SpeciesMove salvo = mapper.paraDominio(repository.save(mapper.paraJpa(speciesMove)));
            unidadeTrabalhoEventosDominio.registrar(speciesMove, salvo.getId());
            return salvo;
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateSpeciesMoveException("Move ja associado ao moveset da especie");
        }
    }

    @Override
    public void remover(SpeciesMove speciesMove) {
        repository.deleteById(speciesMove.getId());
        unidadeTrabalhoEventosDominio.registrar(speciesMove, speciesMove.getId());
    }

    @Override
    public Optional<SpeciesMove> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::paraDominio);
    }

    @Override
    public Optional<SpeciesMove> buscarPorChave(Long speciesId, Long moveId, MoveLearnMethod learnMethod, Integer levelLearnedAt) {
        return repository.findBySpeciesIdAndMoveIdAndLearnMethodAndLevelLearnedAt(speciesId, moveId, learnMethod, levelLearnedAt)
                .map(mapper::paraDominio);
    }

    @Override
    public Optional<SpeciesMove> buscarPrimeiroPorSpeciesIdEMoveId(Long speciesId, Long moveId) {
        return repository.findFirstBySpeciesIdAndMoveIdOrderByIdAsc(speciesId, moveId).map(mapper::paraDominio);
    }

    @Override
    public List<SpeciesMove> listarPorSpeciesIdEMoveId(Long speciesId, Long moveId) {
        return repository.findBySpeciesIdAndMoveIdOrderByIdAsc(speciesId, moveId)
                .stream()
                .map(mapper::paraDominio)
                .toList();
    }

    @Override
    public List<SpeciesMove> listarPorSpeciesId(Long speciesId) {
        return repository.findBySpeciesIdOrderByMoveIdAscLearnMethodAscLevelLearnedAtAsc(speciesId)
                .stream()
                .map(mapper::paraDominio)
                .toList();
    }

    @Override
    public List<SpeciesMove> listarPorMoveId(Long moveId) {
        return repository.findByMoveIdOrderBySpeciesIdAscLearnMethodAscLevelLearnedAtAsc(moveId)
                .stream()
                .map(mapper::paraDominio)
                .toList();
    }

    @Override
    public boolean existePorChave(Long speciesId, Long moveId, MoveLearnMethod learnMethod, Integer levelLearnedAt) {
        return repository.existsBySpeciesIdAndMoveIdAndLearnMethodAndLevelLearnedAt(speciesId, moveId, learnMethod, levelLearnedAt);
    }
}
