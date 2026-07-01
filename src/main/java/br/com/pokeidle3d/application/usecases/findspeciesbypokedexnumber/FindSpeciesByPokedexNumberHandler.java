package br.com.pokeidle3d.application.usecases.findspeciesbypokedexnumber;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.exceptions.SpeciesNotFoundException;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindSpeciesByPokedexNumberHandler implements FindSpeciesByPokedexNumberUseCase {

    private final SpeciesRepository speciesRepository;

    public FindSpeciesByPokedexNumberHandler(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Species handle(FindSpeciesByPokedexNumberQuery query) {
        return speciesRepository.buscarPorPokedexNumber(query.pokedexNumber())
                .orElseThrow(() -> new SpeciesNotFoundException("Especie nao encontrada"));
    }
}
