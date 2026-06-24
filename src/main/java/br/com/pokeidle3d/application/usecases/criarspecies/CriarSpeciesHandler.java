package br.com.pokeidle3d.application.usecases.criarspecies;

import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.exceptions.SpeciesDuplicadaException;
import br.com.pokeidle3d.domain.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarSpeciesHandler implements CriarSpeciesUseCase {

    private final SpeciesRepository speciesRepository;

    public CriarSpeciesHandler(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Transactional
    @Override
    public Species handle(CriarSpeciesCommand command) {
        if (speciesRepository.existePorPokedexNumber(command.pokedexNumber())) {
            throw new SpeciesDuplicadaException("Ja existe especie com este numero da Pokedex");
        }
        if (speciesRepository.existePorName(command.name())) {
            throw new SpeciesDuplicadaException("Ja existe especie com este nome");
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
                command.model3dRef()
        );

        return speciesRepository.salvar(species);
    }
}
