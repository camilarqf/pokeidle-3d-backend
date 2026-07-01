package br.com.pokeidle3d.infra.integrations.pokemon3dapi;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class Pokemon3dApiClientTest {

    @Test
    void deveBuscarModeloRegularPorPokedexNumber() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://pokemon3d.test/v1");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        Pokemon3dApiClient client = new Pokemon3dApiClient(builder.build());

        server.expect(requestTo("https://pokemon3d.test/v1/pokemon"))
                .andRespond(withSuccess("""
                        {
                          "pokemon": [
                            {
                              "id": 1,
                              "forms": [
                                {
                                  "name": "Bulbasaur",
                                  "model": "https://assets.test/models/opt/regular/1.glb",
                                  "formName": "regular"
                                },
                                {
                                  "name": "Shiny Bulbasaur",
                                  "model": "https://assets.test/models/opt/shiny/1.glb",
                                  "formName": "shiny"
                                }
                              ]
                            }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        Pokemon3dModelRef model = client.buscarModeloRegular(1);

        assertThat(model.pokedexNumber()).isEqualTo(1);
        assertThat(model.formName()).isEqualTo("regular");
        assertThat(model.modelUri()).isEqualTo(URI.create("https://assets.test/models/opt/regular/1.glb"));
        server.verify();
    }

    @Test
    void deveFalharQuandoModeloRegularNaoExiste() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://pokemon3d.test/v1");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        Pokemon3dApiClient client = new Pokemon3dApiClient(builder.build());

        server.expect(requestTo("https://pokemon3d.test/v1/pokemon"))
                .andRespond(withSuccess("{\"pokemon\":[]}", MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> client.buscarModeloRegular(999))
                .isInstanceOf(Pokemon3dModelNotFoundException.class)
                .hasMessageContaining("999");
        server.verify();
    }

    @Test
    void deveBaixarModeloGlb() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://pokemon3d.test/v1");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        Pokemon3dApiClient client = new Pokemon3dApiClient(builder.build());

        server.expect(requestTo("https://assets.test/1.glb"))
                .andRespond(withSuccess(new byte[]{1, 2, 3}, MediaType.APPLICATION_OCTET_STREAM));

        assertThat(client.baixarModelo(URI.create("https://assets.test/1.glb"))).containsExactly(1, 2, 3);
        server.verify();
    }
}
