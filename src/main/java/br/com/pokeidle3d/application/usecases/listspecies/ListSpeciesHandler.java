package br.com.pokeidle3d.application.usecases.listspecies;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import br.com.pokeidle3d.domain.valueobjects.PaginatedResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListSpeciesHandler implements ListSpeciesUseCase {

    private final SpeciesRepository speciesRepository;

    public ListSpeciesHandler(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResult<Species> handle(ListSpeciesQuery query) {
        return speciesRepository.listar(query.pagina(), query.tamanho());
    }
}
