package br.com.pokeidle3d.api.mappers;

import br.com.pokeidle3d.api.dtos.CriarSpeciesRequest;
import br.com.pokeidle3d.api.dtos.PaginaResponse;
import br.com.pokeidle3d.api.dtos.SpeciesResponse;
import br.com.pokeidle3d.application.commands.CriarSpeciesCommand;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;
import org.springframework.stereotype.Component;

@Component
public class SpeciesApiMapper {

    public CriarSpeciesCommand paraCommand(CriarSpeciesRequest request) {
        return new CriarSpeciesCommand(
                request.pokedexNumber(),
                request.name(),
                request.primaryType(),
                request.secondaryType(),
                request.baseHp(),
                request.baseAttack(),
                request.baseDefense(),
                request.baseSpecialAttack(),
                request.baseSpecialDefense(),
                request.baseSpeed(),
                request.spriteRef(),
                request.model3dRef()
        );
    }

    public SpeciesResponse paraResponse(Species species) {
        return new SpeciesResponse(
                species.getId(),
                species.getPokedexNumber(),
                species.getName(),
                species.getPrimaryType(),
                species.getSecondaryType(),
                species.getBaseHp(),
                species.getBaseAttack(),
                species.getBaseDefense(),
                species.getBaseSpecialAttack(),
                species.getBaseSpecialDefense(),
                species.getBaseSpeed(),
                species.getSpriteRef(),
                species.getModel3dRef(),
                species.getCreatedAt(),
                species.getUpdatedAt()
        );
    }

    public PaginaResponse<SpeciesResponse> paraPaginaResponse(ResultadoPaginado<Species> pagina) {
        return new PaginaResponse<>(
                pagina.itens().stream().map(this::paraResponse).toList(),
                pagina.totalItens(),
                pagina.totalPaginas(),
                pagina.pagina(),
                pagina.tamanho()
        );
    }
}
