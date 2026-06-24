package br.com.pokeidle3d.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class SpeciesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarConsultarPorIdEConsultarPorPokedexNumber() throws Exception {
        MvcResult resultadoCriacao = mockMvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadBulbasaur()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.pokedexNumber").value(1))
                .andExpect(jsonPath("$.name").value("bulbasaur"))
                .andReturn();

        JsonNode body = objectMapper.readTree(resultadoCriacao.getResponse().getContentAsString());
        long id = body.get("id").asLong();
        assertThat(id).isPositive();

        mockMvc.perform(get("/api/especies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pokedexNumber").value(1))
                .andExpect(jsonPath("$.primaryType").value("GRASS"));

        mockMvc.perform(get("/api/especies/pokedex/{pokedexNumber}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.secondaryType").value("POISON"));
    }

    @Test
    void naoDeveCriarDuasSpeciesComMesmoPokedexNumber() throws Exception {
        mockMvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadCharmander(4, "charmander")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadCharmander(4, "charmander-variant")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Ja existe especie com este numero da Pokedex"));
    }

    @Test
    void naoDeveCriarDuasSpeciesComMesmoNome() throws Exception {
        mockMvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadCharmander(6, "charizard")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadCharmander(66, "Charizard")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Ja existe especie com este nome"));
    }

    @Test
    void naoDeveCriarSpeciesComStatNegativo() throws Exception {
        mockMvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "pokedexNumber": 7,
                                  "name": "squirtle",
                                  "primaryType": "WATER",
                                  "baseHp": -1,
                                  "baseAttack": 48,
                                  "baseDefense": 65,
                                  "baseSpecialAttack": 50,
                                  "baseSpecialDefense": 64,
                                  "baseSpeed": 43,
                                  "spriteRef": "sprite",
                                  "model3dRef": "modelo"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payload invalido"));
    }

    @Test
    void deveListarSpeciesPaginadas() throws Exception {
        mockMvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadCharmander(5, "charmeleon")))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/especies")
                        .param("pagina", "0")
                        .param("tamanho", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens").isArray())
                .andExpect(jsonPath("$.totalItens").isNumber());
    }

    @Test
    void deveRetornarNaoEncontradoQuandoSpeciesNaoExiste() throws Exception {
        mockMvc.perform(get("/api/especies/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Especie nao encontrada"));
    }

    private String payloadBulbasaur() {
        return """
                {
                  "pokedexNumber": 1,
                  "name": "bulbasaur",
                  "primaryType": "GRASS",
                  "secondaryType": "POISON",
                  "baseHp": 45,
                  "baseAttack": 49,
                  "baseDefense": 49,
                  "baseSpecialAttack": 65,
                  "baseSpecialDefense": 65,
                  "baseSpeed": 45,
                  "spriteRef": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
                  "model3dRef": "referencia-ou-url-do-modelo-3d"
                }
                """;
    }

    private String payloadCharmander(int pokedexNumber, String name) {
        return """
                {
                  "pokedexNumber": %d,
                  "name": "%s",
                  "primaryType": "FIRE",
                  "baseHp": 39,
                  "baseAttack": 52,
                  "baseDefense": 43,
                  "baseSpecialAttack": 60,
                  "baseSpecialDefense": 50,
                  "baseSpeed": 65,
                  "spriteRef": "sprite",
                  "model3dRef": "modelo"
                }
                """.formatted(pokedexNumber, name);
    }
}
