package br.com.pokeidle3d.api.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class MoveControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarConsultarPorIdEConsultarPorName() throws Exception {
        MvcResult resultadoCriacao = mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove("tackle", "NORMAL", 40, 100, "PHYSICAL", 35)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("tackle"))
                .andExpect(jsonPath("$.type").value("NORMAL"))
                .andReturn();

        JsonNode body = objectMapper.readTree(resultadoCriacao.getResponse().getContentAsString());
        long id = body.get("id").asLong();
        assertThat(id).isPositive();

        mockMvc.perform(get("/api/moves/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("tackle"))
                .andExpect(jsonPath("$.category").value("PHYSICAL"));

        mockMvc.perform(get("/api/moves/name/{name}", "tackle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.pp").value(35));
    }

    @Test
    void deveListarMovesPaginadosEFiltrarPorTypeECategoria() throws Exception {
        mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove("ember", "FIRE", 40, 100, "SPECIAL", 25)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove("growl", "NORMAL", null, 100, "STATUS", 40)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/moves")
                        .param("pagina", "0")
                        .param("tamanho", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens").isArray())
                .andExpect(jsonPath("$.totalItens").isNumber());

        mockMvc.perform(get("/api/moves")
                        .param("type", "FIRE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens[0].name").value("ember"));

        mockMvc.perform(get("/api/moves")
                        .param("category", "STATUS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens[0].name").value("growl"));
    }

    @Test
    void naoDeveCriarDuasMovesComMesmoNome() throws Exception {
        mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove("quick-attack", "NORMAL", 40, 100, "PHYSICAL", 30)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove("Quick-Attack", "NORMAL", 40, 100, "PHYSICAL", 30)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Ja existe movimento com este nome"));
    }

    @Test
    void naoDeveCriarMoveComPpInvalidoPowerNegativoOuAccuracyInvalida() throws Exception {
        mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove("invalid-pp", "NORMAL", 40, 100, "PHYSICAL", 0)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payload invalido"));

        mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove("invalid-power", "NORMAL", -1, 100, "PHYSICAL", 35)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payload invalido"));

        mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove("invalid-accuracy", "NORMAL", 40, 101, "PHYSICAL", 35)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payload invalido"));
    }

    @Test
    void deveRetornarNaoEncontradoQuandoMoveNaoExiste() throws Exception {
        mockMvc.perform(get("/api/moves/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movimento nao encontrado"));
    }

    @Test
    void deveRetornarPayloadInvalidoParaTipoOuCategoriaInvalidos() throws Exception {
        mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove("bad-type", "INVALID", 40, 100, "PHYSICAL", 35)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payload invalido"));

        mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove("bad-category", "NORMAL", 40, 100, "INVALID", 35)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payload invalido"));
    }

    private String payloadMove(String name, String type, Integer power, Integer accuracy, String category, Integer pp) throws Exception {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("name", name);
        payload.put("type", type);
        payload.put("power", power);
        payload.put("accuracy", accuracy);
        payload.put("category", category);
        payload.put("pp", pp);
        return objectMapper.writeValueAsString(payload);
    }
}
