package br.com.pokeidle3d.infra.integrations.pokeapi;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class PokeApiClient {

    private final RestClient restClient;

    public PokeApiClient(@Qualifier("pokeApiRestClient") RestClient pokeApiRestClient) {
        this.restClient = pokeApiRestClient;
    }

    public PokeApiPokemonResponse buscarPokemon(String idOrName) {
        try {
            PokeApiPokemonResponse response = restClient
                    .get()
                    .uri("/pokemon/{idOrName}", idOrName)
                    .retrieve()
                    .body(PokeApiPokemonResponse.class);

            if (response == null) {
                throw new PokeApiIntegrationException("Resposta vazia da PokeAPI para: " + idOrName);
            }

            return response;
        } catch (HttpClientErrorException.NotFound exception) {
            throw new PokeApiPokemonNotFoundException(idOrName);
        } catch (RestClientException exception) {
            throw new PokeApiIntegrationException("Erro ao consultar PokeAPI para: " + idOrName, exception);
        }
    }
}
