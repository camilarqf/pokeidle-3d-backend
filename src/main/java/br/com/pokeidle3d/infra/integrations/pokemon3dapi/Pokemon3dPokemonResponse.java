package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

import java.util.List;

public record Pokemon3dPokemonResponse(Integer id, List<Pokemon3dFormResponse> forms) {
}
