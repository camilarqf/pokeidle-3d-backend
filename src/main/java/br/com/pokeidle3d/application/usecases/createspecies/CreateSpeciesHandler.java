package br.com.pokeidle3d.application.usecases.createspecies;

import br.com.pokeidle3d.application.events.DomainEventUnitOfWork;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.exceptions.DuplicateSpeciesException;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateSpeciesHandler implements CreateSpeciesUseCase {

    private final SpeciesRepository speciesRepository;
    private final DomainEventUnitOfWork unidadeTrabalhoEventosDominio;

    public CreateSpeciesHandler(
            SpeciesRepository speciesRepository,
            DomainEventUnitOfWork unidadeTrabalhoEventosDominio
    ) {
        this.speciesRepository = speciesRepository;
        this.unidadeTrabalhoEventosDominio = unidadeTrabalhoEventosDominio;
    }

    @Transactional
    @Override
    public Species handle(CreateSpeciesCommand command) {
        if (speciesRepository.existePorPokedexNumber(command.pokedexNumber())) {
            throw new DuplicateSpeciesException("Ja existe especie com este numero da Pokedex");
        }
        if (speciesRepository.existePorName(command.name())) {
            throw new DuplicateSpeciesException("Ja existe especie com este nome");
        }

        Species species = Species.criar(
                command.pokedexNumber(),
                command.name(),
                command.primaryType(),
                command.secondaryType(),
                command.baseHp(),
                command.baseAttack(),
                command.baseDefense(),
                command.baseSpecialAttack(),
                command.baseSpecialDefense(),
                command.baseSpeed(),
                command.spriteRef(),
                command.model3dRef(),
                command.correlationKey()
        );

        Species speciesSalva = speciesRepository.salvar(species);
        unidadeTrabalhoEventosDominio.publicarEventosPendentes();

        return speciesSalva;
    }
}
