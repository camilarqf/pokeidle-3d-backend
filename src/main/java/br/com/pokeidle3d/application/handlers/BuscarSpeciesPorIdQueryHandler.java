package br.com.pokeidle3d.application.handlers;

import br.com.pokeidle3d.application.queries.BuscarSpeciesPorIdQuery;
import br.com.pokeidle3d.application.usecases.BuscarSpeciesPorIdUseCase;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.exceptions.SpeciesNaoEncontradaException;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuscarSpeciesPorIdQueryHandler implements BuscarSpeciesPorIdUseCase {

    private final SpeciesRepository speciesRepository;

    public BuscarSpeciesPorIdQueryHandler(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Species handle(BuscarSpeciesPorIdQuery query) {
        return speciesRepository.buscarPorId(query.id())
                .orElseThrow(() -> new SpeciesNaoEncontradaException("Especie nao encontrada"));
    }
}
