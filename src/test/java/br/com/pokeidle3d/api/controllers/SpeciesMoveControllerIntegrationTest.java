package br.com.pokeidle3d.api.controllers;

import br.com.pokeidle3d.infra.repositories.SpringDataEventJpaRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class SpeciesMoveControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringDataEventJpaRepository eventJpaRepository;

    @Test
    void deveAdicionarListarPorSpeciesListarPorMoveERemoverMoveDoMoveset() throws Exception {
        long speciesId = criarSpecies(101, "moveset-bulbasaur");
        long moveId = criarMove("moveset-vine-whip");

        MvcResult resultadoAdicao = mockMvc.perform(post("/api/species/{speciesId}/moves", speciesId)
                        .header("X-Correlation-Key", "moveset-add-integration-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadSpeciesMove(moveId, "LEVEL_UP", 7)))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-Correlation-Key", "moveset-add-integration-key"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.speciesId").value((int) speciesId))
                .andExpect(jsonPath("$.moveId").value((int) moveId))
                .andExpect(jsonPath("$.learnMethod").value("LEVEL_UP"))
                .andExpect(jsonPath("$.levelLearnedAt").value(7))
                .andReturn();

        JsonNode body = objectMapper.readTree(resultadoAdicao.getResponse().getContentAsString());
        assertThat(body.get("id").asLong()).isPositive();
        assertThat(eventJpaRepository.findAll())
                .anySatisfy(evento -> {
                    assertThat(evento.getFullName()).endsWith("MoveAdicionadoAoMovesetSpeciesEvent");
                    assertThat(evento.getAggregateType()).isEqualTo("SpeciesMove");
                    assertThat(evento.getPayload()).contains("moveset-add-integration-key");
                });

        mockMvc.perform(get("/api/especies/{speciesId}/moves", speciesId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].move.id").value((int) moveId))
                .andExpect(jsonPath("$[0].move.name").value("moveset-vine-whip"))
                .andExpect(jsonPath("$[0].learnMethod").value("LEVEL_UP"));

        mockMvc.perform(get("/api/moves/{moveId}/species", moveId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].species.id").value((int) speciesId))
                .andExpect(jsonPath("$[0].species.name").value("moveset-bulbasaur"))
                .andExpect(jsonPath("$[0].levelLearnedAt").value(7));

        mockMvc.perform(delete("/api/species/{speciesId}/moves/{moveId}", speciesId, moveId)
                        .header("X-Correlation-Key", "moveset-remove-integration-key"))
                .andExpect(status().isNoContent())
                .andExpect(header().string("X-Correlation-Key", "moveset-remove-integration-key"));

        assertThat(eventJpaRepository.findAll())
                .anySatisfy(evento -> {
                    assertThat(evento.getFullName()).endsWith("MoveRemovidoDoMovesetSpeciesEvent");
                    assertThat(evento.getPayload()).contains("moveset-remove-integration-key");
                });

        mockMvc.perform(get("/api/species/{speciesId}/moves", speciesId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void naoDeveAdicionarAssociacaoDuplicada() throws Exception {
        long speciesId = criarSpecies(102, "moveset-charmander");
        long moveId = criarMove("moveset-ember");

        mockMvc.perform(post("/api/species/{speciesId}/moves", speciesId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadSpeciesMove(moveId, "TM", null)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/species/{speciesId}/moves", speciesId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadSpeciesMove(moveId, "TM", null)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Move ja associado ao moveset da especie"));
    }

    @Test
    void naoDeveAdicionarLevelUpSemLevelOuMoveInexistente() throws Exception {
        long speciesId = criarSpecies(103, "moveset-squirtle");

        mockMvc.perform(post("/api/species/{speciesId}/moves", speciesId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadSpeciesMove(99999L, "LEVEL_UP", null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payload invalido"));

        mockMvc.perform(post("/api/species/{speciesId}/moves", speciesId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadSpeciesMove(99999L, "TM", null)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movimento nao encontrado"));
    }

    @Test
    void naoDeveAdicionarSpeciesInexistenteOuRemoverAssociacaoInexistente() throws Exception {
        long moveId = criarMove("moveset-water-gun");

        mockMvc.perform(post("/api/species/{speciesId}/moves", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadSpeciesMove(moveId, "TM", null)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Especie nao encontrada"));

        long speciesId = criarSpecies(104, "moveset-pikachu");

        mockMvc.perform(delete("/api/species/{speciesId}/moves/{moveId}", speciesId, moveId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Move nao associado ao moveset da especie"));
    }

    private long criarSpecies(int pokedexNumber, String name) throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/species")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadSpecies(pokedexNumber, name)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(resultado.getResponse().getContentAsString()).get("id").asLong();
    }

    private long criarMove(String name) throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadMove(name)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(resultado.getResponse().getContentAsString()).get("id").asLong();
    }

    private String payloadSpecies(int pokedexNumber, String name) throws Exception {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("pokedexNumber", pokedexNumber);
        payload.put("name", name);
        payload.put("primaryType", "GRASS");
        payload.put("secondaryType", null);
        payload.put("baseHp", 45);
        payload.put("baseAttack", 49);
        payload.put("baseDefense", 49);
        payload.put("baseSpecialAttack", 65);
        payload.put("baseSpecialDefense", 65);
        payload.put("baseSpeed", 45);
        payload.put("spriteRef", "sprite");
        payload.put("model3dRef", "modelo");
        return objectMapper.writeValueAsString(payload);
    }

    private String payloadMove(String name) throws Exception {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("name", name);
        payload.put("type", "NORMAL");
        payload.put("power", 40);
        payload.put("accuracy", 100);
        payload.put("category", "PHYSICAL");
        payload.put("pp", 35);
        return objectMapper.writeValueAsString(payload);
    }

    private String payloadSpeciesMove(Long moveId, String learnMethod, Integer levelLearnedAt) throws Exception {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("moveId", moveId);
        payload.put("learnMethod", learnMethod);
        payload.put("levelLearnedAt", levelLearnedAt);
        return objectMapper.writeValueAsString(payload);
    }
}
