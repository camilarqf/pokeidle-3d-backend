package br.com.pokeidle3d.application.usecases.findspeciesbyid;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.exceptions.SpeciesNotFoundException;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindSpeciesByIdHandler implements FindSpeciesByIdUseCase {

    private final SpeciesRepository speciesRepository;

    public FindSpeciesByIdHandler(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Species handle(FindSpeciesByIdQuery query) {
        return speciesRepository.buscarPorId(query.id())
                .orElseThrow(() -> new SpeciesNotFoundException("Especie nao encontrada"));
    }
}
