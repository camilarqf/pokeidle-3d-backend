package br.com.pokeidle3d.application.usecases.listarspecies;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListarSpeciesHandler implements ListarSpeciesUseCase {

    private final SpeciesRepository speciesRepository;

    public ListarSpeciesHandler(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public ResultadoPaginado<Species> handle(ListarSpeciesQuery query) {
        return speciesRepository.listar(query.pagina(), query.tamanho());
    }
}
