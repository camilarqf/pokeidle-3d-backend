package br.com.pokeidle3d.api.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class OpenApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveExporOpenApiJson() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Correlation-Key", org.hamcrest.Matchers.notNullValue()))
                .andExpect(jsonPath("$.openapi").value("3.0.3"))
                .andExpect(jsonPath("$.info.title").value("Poke Idle 3D API"))
                .andExpect(jsonPath("$.paths['/api/species']").exists())
                .andExpect(jsonPath("$.paths['/api/moves']").exists())
                .andExpect(jsonPath("$.components.schemas.SpeciesResponse").exists());
    }

    @Test
    void deveExporSwaggerUiApontandoParaSpecOpenApi() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("url: \"/v3/api-docs\"")));
    }

    @Test
    void deveSobrescreverIndexDoWebjarApontandoParaSpecOpenApi() throws Exception {
        mockMvc.perform(get("/webjars/swagger-ui/5.17.14/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("url: \"/v3/api-docs\"")));
    }
}
