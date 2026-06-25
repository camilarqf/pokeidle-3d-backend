package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

import java.net.URI;

public record Pokemon3dModelRef(Integer pokedexNumber, String name, String formName, URI modelUri) {
}
