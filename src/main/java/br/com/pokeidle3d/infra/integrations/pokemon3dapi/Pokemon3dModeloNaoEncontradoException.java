package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

public class Pokemon3dModeloNaoEncontradoException extends Pokemon3dApiIntegracaoException {

    public Pokemon3dModeloNaoEncontradoException(Integer pokedexNumber) {
        super("Modelo 3D regular nao encontrado para Pokedex: " + pokedexNumber);
    }
}
