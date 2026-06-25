package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

import java.util.List;

public record Pokemon3dCatalogResponse(List<Pokemon3dPokemonResponse> pokemon) {
}
