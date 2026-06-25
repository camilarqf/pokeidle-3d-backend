package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.Comparator;

@Component
public class Pokemon3dApiClient {

    private static final String REGULAR_FORM = "regular";

    private final RestClient restClient;

    public Pokemon3dApiClient(@Qualifier("pokemon3dApiRestClient") RestClient pokemon3dApiRestClient) {
        this.restClient = pokemon3dApiRestClient;
    }

    public Pokemon3dModelRef buscarModeloRegular(Integer pokedexNumber) {
        Pokemon3dCatalogResponse catalog = buscarCatalogo();
        if (catalog.pokemon() == null) {
            throw new Pokemon3dModeloNaoEncontradoException(pokedexNumber);
        }

        return catalog.pokemon()
                .stream()
                .filter(pokemon -> pokedexNumber.equals(pokemon.id()))
                .flatMap(pokemon -> pokemon.forms() == null ? java.util.stream.Stream.empty() : pokemon.forms().stream())
                .filter(form -> REGULAR_FORM.equalsIgnoreCase(form.formName()))
                .filter(form -> form.model() != null && form.model().toLowerCase().endsWith(".glb"))
                .min(Comparator.comparing(Pokemon3dFormResponse::name))
                .map(form -> new Pokemon3dModelRef(pokedexNumber, form.name(), form.formName(), URI.create(form.model())))
                .orElseThrow(() -> new Pokemon3dModeloNaoEncontradoException(pokedexNumber));
    }

    public byte[] baixarModelo(URI modelUri) {
        try {
            byte[] bytes = restClient
                    .get()
                    .uri(modelUri)
                    .retrieve()
                    .body(byte[].class);
            if (bytes == null || bytes.length == 0) {
                throw new Pokemon3dApiIntegracaoException("Modelo 3D vazio: " + modelUri);
            }
            return bytes;
        } catch (RestClientException exception) {
            throw new Pokemon3dApiIntegracaoException("Erro ao baixar modelo 3D: " + modelUri, exception);
        }
    }

    private Pokemon3dCatalogResponse buscarCatalogo() {
        try {
            Pokemon3dCatalogResponse response = restClient
                    .get()
                    .uri("/pokemon")
                    .retrieve()
                    .body(Pokemon3dCatalogResponse.class);
            if (response == null) {
                throw new Pokemon3dApiIntegracaoException("Catalogo vazio da Pokemon-3D-api");
            }
            return response;
        } catch (RestClientException exception) {
            throw new Pokemon3dApiIntegracaoException("Erro ao consultar catalogo da Pokemon-3D-api", exception);
        }
    }
}
