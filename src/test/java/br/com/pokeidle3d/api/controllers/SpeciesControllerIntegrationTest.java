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
                        .content(payloadSpecies(7, "squirtle", "WATER", null, -1, 48, 65, 50, 64, 43)))
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

    private String payloadBulbasaur() throws Exception {
        Map<String, Object> payload = payloadSpeciesBase(1, "bulbasaur", "GRASS", "POISON", 45, 49, 49, 65, 65, 45);
        payload.put("spriteRef", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png");
        payload.put("model3dRef", "referencia-ou-url-do-modelo-3d");
        return objectMapper.writeValueAsString(payload);
    }

    private String payloadCharmander(int pokedexNumber, String name) throws Exception {
        return payloadSpecies(pokedexNumber, name, "FIRE", null, 39, 52, 43, 60, 50, 65);
    }

    private String payloadSpecies(
            int pokedexNumber,
            String name,
            String primaryType,
            String secondaryType,
            int baseHp,
            int baseAttack,
            int baseDefense,
            int baseSpecialAttack,
            int baseSpecialDefense,
            int baseSpeed
    ) throws Exception {
        Map<String, Object> payload = payloadSpeciesBase(
                pokedexNumber,
                name,
                primaryType,
                secondaryType,
                baseHp,
                baseAttack,
                baseDefense,
                baseSpecialAttack,
                baseSpecialDefense,
                baseSpeed
        );
        payload.put("spriteRef", "sprite");
        payload.put("model3dRef", "modelo");
        return objectMapper.writeValueAsString(payload);
    }

    private Map<String, Object> payloadSpeciesBase(
            int pokedexNumber,
            String name,
            String primaryType,
            String secondaryType,
            int baseHp,
            int baseAttack,
            int baseDefense,
            int baseSpecialAttack,
            int baseSpecialDefense,
            int baseSpeed
    ) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("pokedexNumber", pokedexNumber);
        payload.put("name", name);
        payload.put("primaryType", primaryType);
        payload.put("secondaryType", secondaryType);
        payload.put("baseHp", baseHp);
        payload.put("baseAttack", baseAttack);
        payload.put("baseDefense", baseDefense);
        payload.put("baseSpecialAttack", baseSpecialAttack);
        payload.put("baseSpecialDefense", baseSpecialDefense);
        payload.put("baseSpeed", baseSpeed);
        return payload;
    }
}
