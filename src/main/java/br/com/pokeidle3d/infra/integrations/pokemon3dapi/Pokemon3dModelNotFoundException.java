package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

public class Pokemon3dModelNotFoundException extends Pokemon3dApiIntegrationException {

    public Pokemon3dModelNotFoundException(Integer pokedexNumber) {
        super("Modelo 3D regular nao encontrado para Pokedex: " + pokedexNumber);
    }
}
