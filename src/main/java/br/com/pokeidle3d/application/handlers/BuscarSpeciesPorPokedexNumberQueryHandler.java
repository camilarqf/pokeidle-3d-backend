package br.com.pokeidle3d.application.handlers;

import br.com.pokeidle3d.application.queries.BuscarSpeciesPorPokedexNumberQuery;
import br.com.pokeidle3d.application.usecases.BuscarSpeciesPorPokedexNumberUseCase;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.exceptions.SpeciesNaoEncontradaException;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuscarSpeciesPorPokedexNumberQueryHandler implements BuscarSpeciesPorPokedexNumberUseCase {

    private final SpeciesRepository speciesRepository;

    public BuscarSpeciesPorPokedexNumberQueryHandler(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Species handle(BuscarSpeciesPorPokedexNumberQuery query) {
        return speciesRepository.buscarPorPokedexNumber(query.pokedexNumber())
                .orElseThrow(() -> new SpeciesNaoEncontradaException("Especie nao encontrada"));
    }
}
