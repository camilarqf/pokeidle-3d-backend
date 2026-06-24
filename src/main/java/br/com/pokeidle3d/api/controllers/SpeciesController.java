package br.com.pokeidle3d.api.controllers;

import br.com.pokeidle3d.api.dtos.CriarSpeciesRequest;
import br.com.pokeidle3d.api.dtos.PaginaResponse;
import br.com.pokeidle3d.api.dtos.SpeciesResponse;
import br.com.pokeidle3d.api.mappers.SpeciesApiMapper;
import br.com.pokeidle3d.application.queries.BuscarSpeciesPorIdQuery;
import br.com.pokeidle3d.application.queries.BuscarSpeciesPorPokedexNumberQuery;
import br.com.pokeidle3d.application.queries.ListarSpeciesQuery;
import br.com.pokeidle3d.application.usecases.BuscarSpeciesPorIdUseCase;
import br.com.pokeidle3d.application.usecases.BuscarSpeciesPorPokedexNumberUseCase;
import br.com.pokeidle3d.application.usecases.CriarSpeciesUseCase;
import br.com.pokeidle3d.application.usecases.ListarSpeciesUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/especies")
public class SpeciesController {

    private final CriarSpeciesUseCase criarSpeciesUseCase;
    private final BuscarSpeciesPorIdUseCase buscarSpeciesPorIdUseCase;
    private final BuscarSpeciesPorPokedexNumberUseCase buscarSpeciesPorPokedexNumberUseCase;
    private final ListarSpeciesUseCase listarSpeciesUseCase;
    private final SpeciesApiMapper mapper;

    public SpeciesController(
            CriarSpeciesUseCase criarSpeciesUseCase,
            BuscarSpeciesPorIdUseCase buscarSpeciesPorIdUseCase,
            BuscarSpeciesPorPokedexNumberUseCase buscarSpeciesPorPokedexNumberUseCase,
            ListarSpeciesUseCase listarSpeciesUseCase,
            SpeciesApiMapper mapper
    ) {
        this.criarSpeciesUseCase = criarSpeciesUseCase;
        this.buscarSpeciesPorIdUseCase = buscarSpeciesPorIdUseCase;
        this.buscarSpeciesPorPokedexNumberUseCase = buscarSpeciesPorPokedexNumberUseCase;
        this.listarSpeciesUseCase = listarSpeciesUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<SpeciesResponse> criar(@Valid @RequestBody CriarSpeciesRequest request) {
        SpeciesResponse response = mapper.paraResponse(criarSpeciesUseCase.handle(mapper.paraCommand(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public SpeciesResponse buscarPorId(@PathVariable @Min(1) Long id) {
        return mapper.paraResponse(buscarSpeciesPorIdUseCase.handle(new BuscarSpeciesPorIdQuery(id)));
    }

    @GetMapping("/pokedex/{pokedexNumber}")
    public SpeciesResponse buscarPorPokedexNumber(@PathVariable @Min(1) Integer pokedexNumber) {
        return mapper.paraResponse(buscarSpeciesPorPokedexNumberUseCase.handle(
                new BuscarSpeciesPorPokedexNumberQuery(pokedexNumber)
        ));
    }

    @GetMapping
    public PaginaResponse<SpeciesResponse> listar(
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20") @Min(1) int tamanho
    ) {
        return mapper.paraPaginaResponse(listarSpeciesUseCase.handle(new ListarSpeciesQuery(pagina, tamanho)));
    }
}
