package br.com.pokeidle3d.infra.integrations.pokeapi;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

class PokeApiClientTest {

    @Test
    void deveTratarPokemonNaoEncontrado() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://pokeapi.test/api/v2");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        PokeApiClient client = new PokeApiClient(builder.build());

        server.expect(requestTo("https://pokeapi.test/api/v2/pokemon/missingno"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> client.buscarPokemon("missingno"))
                .isInstanceOf(PokeApiPokemonNaoEncontradoException.class)
                .hasMessageContaining("missingno");

        server.verify();
    }
}
