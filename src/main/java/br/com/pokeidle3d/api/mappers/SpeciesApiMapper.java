package br.com.pokeidle3d.api.mappers;

import br.com.pokeidle3d.api.contracts.CriarSpeciesRequest;
import br.com.pokeidle3d.api.contracts.PaginaResponse;
import br.com.pokeidle3d.api.contracts.SpeciesResponse;
import br.com.pokeidle3d.application.usecases.criarspecies.CriarSpeciesCommand;
import br.com.pokeidle3d.domain.entities.Species;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import br.com.pokeidle3d.domain.valueobjects.ResultadoPaginado;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class SpeciesApiMapper {

    private static final String MODEL_3D_REF_PREFIX = "pokemon-3d-api:";
    private static final String LOCAL_MODEL_STORAGE_PREFIX = "storage/assets/3d/pokemon/";
    private static final String LOCAL_MODEL_PUBLIC_PREFIX = "/assets/3d/pokemon/";
    private static final String POKEMON_3D_ASSETS_BASE_URL =
            "https://raw.githubusercontent.com/Pokemon-3D-api/assets/main/models/opt/regular/";

    public CriarSpeciesCommand paraCommand(CriarSpeciesRequest request, CorrelationKey correlationKey) {
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
                request.model3dRef(),
                correlationKey
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
                resolverModel3dRef(species),
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

    private String resolverModel3dRef(Species species) {
        String model3dRef = species.getModel3dRef();
        if (model3dRef == null || model3dRef.isBlank()) {
            return null;
        }

        String trimmed = model3dRef.trim().replace('\\', '/');
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("/")) {
            return trimmed;
        }
        if (trimmed.startsWith(LOCAL_MODEL_STORAGE_PREFIX)) {
            return resolverUrlPublicaLocal(LOCAL_MODEL_PUBLIC_PREFIX + trimmed.substring(LOCAL_MODEL_STORAGE_PREFIX.length()));
        }
        if (trimmed.startsWith(MODEL_3D_REF_PREFIX) && species.getPokedexNumber() != null) {
            return POKEMON_3D_ASSETS_BASE_URL + species.getPokedexNumber() + ".glb";
        }
        return trimmed;
    }

    private String resolverUrlPublicaLocal(String publicPath) {
        try {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(publicPath)
                    .toUriString();
        } catch (IllegalStateException exception) {
            return publicPath;
        }
    }
}
