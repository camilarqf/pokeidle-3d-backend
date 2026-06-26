package br.com.pokeidle3d.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OpenApiController {

    @GetMapping("/v3/api-docs")
    public Map<String, Object> openApi() {
        Map<String, Object> spec = map();
        spec.put("openapi", "3.0.3");
        spec.put("info", map(
                "title", "Poke Idle 3D API",
                "version", "v1",
                "description", "API para especies, movimentos, movesets e referencias de assets 3D."
        ));
        spec.put("servers", List.of(map("url", "/", "description", "Servidor atual")));
        spec.put("tags", List.of(
                map("name", "Species", "description", "Cadastro e consulta de especies Pokemon"),
                map("name", "Moves", "description", "Cadastro e consulta de movimentos"),
                map("name", "Moveset", "description", "Associacao entre especies e movimentos")
        ));
        spec.put("paths", paths());
        spec.put("components", components());
        return spec;
    }

    private Map<String, Object> paths() {
        return map(
                "/api/species", map(
                        "get", operation(
                                "Species",
                                "Listar especies",
                                "Retorna especies paginadas.",
                                List.of(query("pagina", "integer", 0, "Pagina iniciando em zero."), query("tamanho", "integer", 20, "Quantidade de itens por pagina.")),
                                null,
                                ok(pageSchema("SpeciesResponse"))
                        ),
                        "post", operation(
                                "Species",
                                "Criar especie",
                                "Cria uma especie Pokemon.",
                                List.of(),
                                ref("CriarSpeciesRequest"),
                                created(ref("SpeciesResponse"))
                        )
                ),
                "/api/species/{id}", map(
                        "get", operation(
                                "Species",
                                "Buscar especie por id",
                                "Retorna uma especie pelo id interno.",
                                List.of(path("id", "integer", "Id da especie.")),
                                null,
                                ok(ref("SpeciesResponse"))
                        )
                ),
                "/api/species/pokedex/{pokedexNumber}", map(
                        "get", operation(
                                "Species",
                                "Buscar especie por Pokedex",
                                "Retorna uma especie pelo numero da Pokedex.",
                                List.of(path("pokedexNumber", "integer", "Numero da Pokedex.")),
                                null,
                                ok(ref("SpeciesResponse"))
                        )
                ),
                "/api/species/{speciesId}/moves", map(
                        "get", operation(
                                "Moveset",
                                "Listar moveset da especie",
                                "Retorna os movimentos associados a uma especie.",
                                List.of(path("speciesId", "integer", "Id da especie.")),
                                null,
                                ok(arrayOf(ref("MovesetSpeciesResponse")))
                        ),
                        "post", operation(
                                "Moveset",
                                "Adicionar move ao moveset",
                                "Associa um movimento ao moveset de uma especie.",
                                List.of(path("speciesId", "integer", "Id da especie.")),
                                ref("AdicionarMoveAoMovesetSpeciesRequest"),
                                created(ref("SpeciesMoveResponse"))
                        )
                ),
                "/api/species/{speciesId}/moves/{moveId}", map(
                        "delete", operation(
                                "Moveset",
                                "Remover move do moveset",
                                "Remove a associacao entre especie e movimento.",
                                List.of(path("speciesId", "integer", "Id da especie."), path("moveId", "integer", "Id do movimento.")),
                                null,
                                noContent()
                        )
                ),
                "/api/moves", map(
                        "get", operation(
                                "Moves",
                                "Listar movimentos",
                                "Retorna movimentos paginados. Use apenas type ou category, nunca os dois juntos.",
                                List.of(
                                        query("pagina", "integer", 0, "Pagina iniciando em zero."),
                                        query("tamanho", "integer", 20, "Quantidade de itens por pagina."),
                                        enumQuery("type", "PokemonType", "Filtro por tipo Pokemon."),
                                        enumQuery("category", "MoveCategory", "Filtro por categoria.")
                                ),
                                null,
                                ok(pageSchema("MoveResponse"))
                        ),
                        "post", operation(
                                "Moves",
                                "Criar movimento",
                                "Cria um movimento Pokemon.",
                                List.of(),
                                ref("CriarMoveRequest"),
                                created(ref("MoveResponse"))
                        )
                ),
                "/api/moves/{id}", map(
                        "get", operation(
                                "Moves",
                                "Buscar movimento por id",
                                "Retorna um movimento pelo id interno.",
                                List.of(path("id", "integer", "Id do movimento.")),
                                null,
                                ok(ref("MoveResponse"))
                        )
                ),
                "/api/moves/name/{name}", map(
                        "get", operation(
                                "Moves",
                                "Buscar movimento por nome",
                                "Retorna um movimento pelo nome.",
                                List.of(path("name", "string", "Nome do movimento.")),
                                null,
                                ok(ref("MoveResponse"))
                        )
                ),
                "/api/moves/{moveId}/species", map(
                        "get", operation(
                                "Moveset",
                                "Listar especies por movimento",
                                "Retorna as especies que aprendem um movimento.",
                                List.of(path("moveId", "integer", "Id do movimento.")),
                                null,
                                ok(arrayOf(ref("SpeciesPorMoveResponse")))
                        )
                )
        );
    }

    private Map<String, Object> components() {
        return map(
                "parameters", map(
                        "CorrelationKeyHeader", map(
                                "name", "X-Correlation-Key",
                                "in", "header",
                                "required", false,
                                "description", "Chave opcional de correlacao retornada tambem na resposta.",
                                "schema", map("type", "string")
                        )
                ),
                "schemas", map(
                        "PokemonType", stringEnum("NORMAL", "FIRE", "WATER", "ELECTRIC", "GRASS", "ICE", "FIGHTING", "POISON", "GROUND", "FLYING", "PSYCHIC", "BUG", "ROCK", "GHOST", "DRAGON", "DARK", "STEEL", "FAIRY"),
                        "MoveCategory", stringEnum("PHYSICAL", "SPECIAL", "STATUS"),
                        "MoveLearnMethod", stringEnum("LEVEL_UP", "TM", "HM", "TUTOR", "EGG", "EVENT"),
                        "CriarSpeciesRequest", object(required("pokedexNumber", "name", "primaryType", "baseHp", "baseAttack", "baseDefense", "baseSpecialAttack", "baseSpecialDefense", "baseSpeed"), map(
                                "pokedexNumber", integer("int32", 1),
                                "name", string(),
                                "primaryType", ref("PokemonType"),
                                "secondaryType", nullableRef("PokemonType"),
                                "baseHp", integer("int32", 0),
                                "baseAttack", integer("int32", 0),
                                "baseDefense", integer("int32", 0),
                                "baseSpecialAttack", integer("int32", 0),
                                "baseSpecialDefense", integer("int32", 0),
                                "baseSpeed", integer("int32", 0),
                                "spriteRef", nullableString(),
                                "model3dRef", nullableString()
                        )),
                        "CriarMoveRequest", object(required("name", "type", "category", "pp"), map(
                                "name", string(),
                                "type", ref("PokemonType"),
                                "power", nullableInteger("int32", 0),
                                "accuracy", map("type", "integer", "format", "int32", "minimum", 1, "maximum", 100, "nullable", true),
                                "category", ref("MoveCategory"),
                                "pp", integer("int32", 1)
                        )),
                        "AdicionarMoveAoMovesetSpeciesRequest", object(required("moveId", "learnMethod"), map(
                                "moveId", integer("int64", 1),
                                "learnMethod", ref("MoveLearnMethod"),
                                "levelLearnedAt", nullableInteger("int32", 1)
                        )),
                        "SpeciesResponse", object(List.of(), map(
                                "id", integer("int64", 1),
                                "pokedexNumber", integer("int32", 1),
                                "name", string(),
                                "primaryType", ref("PokemonType"),
                                "secondaryType", nullableRef("PokemonType"),
                                "baseHp", integer("int32", 0),
                                "baseAttack", integer("int32", 0),
                                "baseDefense", integer("int32", 0),
                                "baseSpecialAttack", integer("int32", 0),
                                "baseSpecialDefense", integer("int32", 0),
                                "baseSpeed", integer("int32", 0),
                                "spriteRef", nullableString(),
                                "model3dRef", nullableString(),
                                "createdAt", instant(),
                                "updatedAt", instant()
                        )),
                        "MoveResponse", object(List.of(), map(
                                "id", integer("int64", 1),
                                "name", string(),
                                "type", ref("PokemonType"),
                                "power", nullableInteger("int32", 0),
                                "accuracy", nullableInteger("int32", 1),
                                "category", ref("MoveCategory"),
                                "pp", integer("int32", 1),
                                "createdAt", instant(),
                                "updatedAt", instant()
                        )),
                        "SpeciesMoveResponse", object(List.of(), map(
                                "id", integer("int64", 1),
                                "speciesId", integer("int64", 1),
                                "moveId", integer("int64", 1),
                                "learnMethod", ref("MoveLearnMethod"),
                                "levelLearnedAt", nullableInteger("int32", 1),
                                "createdAt", instant(),
                                "updatedAt", instant()
                        )),
                        "MovesetSpeciesResponse", object(List.of(), map(
                                "id", integer("int64", 1),
                                "speciesId", integer("int64", 1),
                                "move", ref("MoveResponse"),
                                "learnMethod", ref("MoveLearnMethod"),
                                "levelLearnedAt", nullableInteger("int32", 1),
                                "createdAt", instant(),
                                "updatedAt", instant()
                        )),
                        "SpeciesPorMoveResponse", object(List.of(), map(
                                "id", integer("int64", 1),
                                "moveId", integer("int64", 1),
                                "species", ref("SpeciesResponse"),
                                "learnMethod", ref("MoveLearnMethod"),
                                "levelLearnedAt", nullableInteger("int32", 1),
                                "createdAt", instant(),
                                "updatedAt", instant()
                        )),
                        "ErroResponse", object(List.of(), map(
                                "timestamp", instant(),
                                "status", integer("int32", 0),
                                "error", string(),
                                "message", string(),
                                "path", string(),
                                "correlationKey", string(),
                                "details", arrayOf(string())
                        ))
                )
        );
    }

    private Map<String, Object> operation(String tag, String summary, String description, List<Map<String, Object>> parameters, Map<String, Object> requestSchema, Map<String, Object> successResponse) {
        Map<String, Object> operation = map();
        operation.put("tags", List.of(tag));
        operation.put("summary", summary);
        operation.put("description", description);
        operation.put("parameters", withCorrelationKey(parameters));
        if (requestSchema != null) {
            operation.put("requestBody", map(
                    "required", true,
                    "content", map("application/json", map("schema", requestSchema))
            ));
        }
        operation.put("responses", responses(successResponse));
        return operation;
    }

    private Map<String, Object> responses(Map<String, Object> successResponse) {
        Map<String, Object> responses = map();
        responses.putAll(successResponse);
        responses.put("400", jsonResponse("Requisicao invalida", ref("ErroResponse")));
        responses.put("404", jsonResponse("Recurso nao encontrado", ref("ErroResponse")));
        responses.put("409", jsonResponse("Conflito de dominio", ref("ErroResponse")));
        return responses;
    }

    private List<Map<String, Object>> withCorrelationKey(List<Map<String, Object>> parameters) {
        java.util.ArrayList<Map<String, Object>> all = new java.util.ArrayList<>();
        all.add(map("$ref", "#/components/parameters/CorrelationKeyHeader"));
        all.addAll(parameters);
        return all;
    }

    private Map<String, Object> ok(Map<String, Object> schema) {
        return map("200", jsonResponse("Sucesso", schema));
    }

    private Map<String, Object> created(Map<String, Object> schema) {
        return map("201", jsonResponse("Criado", schema));
    }

    private Map<String, Object> noContent() {
        return map("204", map("description", "Removido com sucesso"));
    }

    private Map<String, Object> jsonResponse(String description, Map<String, Object> schema) {
        return map("description", description, "content", map("application/json", map("schema", schema)));
    }

    private Map<String, Object> path(String name, String type, String description) {
        return parameter(name, "path", true, type, null, description);
    }

    private Map<String, Object> query(String name, String type, Object defaultValue, String description) {
        return parameter(name, "query", false, type, defaultValue, description);
    }

    private Map<String, Object> enumQuery(String name, String schemaName, String description) {
        return map("name", name, "in", "query", "required", false, "description", description, "schema", ref(schemaName));
    }

    private Map<String, Object> parameter(String name, String in, boolean required, String type, Object defaultValue, String description) {
        Map<String, Object> schema = map("type", type);
        if (defaultValue != null) {
            schema.put("default", defaultValue);
        }
        return map("name", name, "in", in, "required", required, "description", description, "schema", schema);
    }

    private Map<String, Object> pageSchema(String itemSchemaName) {
        return object(List.of(), map(
                "itens", arrayOf(ref(itemSchemaName)),
                "totalItens", integer("int64", 0),
                "totalPaginas", integer("int32", 0),
                "pagina", integer("int32", 0),
                "tamanho", integer("int32", 1)
        ));
    }

    private Map<String, Object> object(List<String> required, Map<String, Object> properties) {
        Map<String, Object> schema = map("type", "object", "properties", properties);
        if (!required.isEmpty()) {
            schema.put("required", required);
        }
        return schema;
    }

    private Map<String, Object> ref(String schemaName) {
        return map("$ref", "#/components/schemas/" + schemaName);
    }

    private Map<String, Object> nullableRef(String schemaName) {
        return map("allOf", List.of(ref(schemaName)), "nullable", true);
    }

    private Map<String, Object> arrayOf(Map<String, Object> itemSchema) {
        return map("type", "array", "items", itemSchema);
    }

    private Map<String, Object> stringEnum(String... values) {
        return map("type", "string", "enum", List.of(values));
    }

    private Map<String, Object> string() {
        return map("type", "string");
    }

    private Map<String, Object> nullableString() {
        return map("type", "string", "nullable", true);
    }

    private Map<String, Object> integer(String format, int minimum) {
        return map("type", "integer", "format", format, "minimum", minimum);
    }

    private Map<String, Object> nullableInteger(String format, int minimum) {
        return map("type", "integer", "format", format, "minimum", minimum, "nullable", true);
    }

    private Map<String, Object> instant() {
        return map("type", "string", "format", "date-time");
    }

    private List<String> required(String... values) {
        return List.of(values);
    }

    private Map<String, Object> map(Object... entries) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            map.put((String) entries[i], entries[i + 1]);
        }
        return map;
    }
}
