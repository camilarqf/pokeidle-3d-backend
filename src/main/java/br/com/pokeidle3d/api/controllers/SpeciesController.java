package br.com.pokeidle3d.api.controllers;

import br.com.pokeidle3d.api.contracts.CriarSpeciesRequest;
import br.com.pokeidle3d.api.contracts.AdicionarMoveAoMovesetSpeciesRequest;
import br.com.pokeidle3d.api.contracts.MovesetSpeciesResponse;
import br.com.pokeidle3d.api.contracts.PaginaResponse;
import br.com.pokeidle3d.api.contracts.SpeciesMoveResponse;
import br.com.pokeidle3d.api.contracts.SpeciesResponse;
import br.com.pokeidle3d.api.context.CorrelationKeyContext;
import br.com.pokeidle3d.api.mappers.SpeciesApiMapper;
import br.com.pokeidle3d.api.mappers.SpeciesMoveApiMapper;
import br.com.pokeidle3d.application.bus.CommandBus;
import br.com.pokeidle3d.application.bus.QueryBus;
import br.com.pokeidle3d.application.usecases.listarmovesetdaspecies.ListarMovesetDaSpeciesQuery;
import br.com.pokeidle3d.application.usecases.removermovedomovesetspecies.RemoverMoveDoMovesetSpeciesCommand;
import br.com.pokeidle3d.application.usecases.buscarspeciesporid.BuscarSpeciesPorIdQuery;
import br.com.pokeidle3d.application.usecases.buscarspeciesporpokedexnumber.BuscarSpeciesPorPokedexNumberQuery;
import br.com.pokeidle3d.application.usecases.listarspecies.ListarSpeciesQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping({"/api/especies", "/api/species"})
public class SpeciesController {

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final SpeciesApiMapper mapper;
    private final SpeciesMoveApiMapper speciesMoveApiMapper;
    private final CorrelationKeyContext correlationKeyContext;

    public SpeciesController(
            CommandBus commandBus,
            QueryBus queryBus,
            SpeciesApiMapper mapper,
            SpeciesMoveApiMapper speciesMoveApiMapper,
            CorrelationKeyContext correlationKeyContext
    ) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.mapper = mapper;
        this.speciesMoveApiMapper = speciesMoveApiMapper;
        this.correlationKeyContext = correlationKeyContext;
    }

    @PostMapping
    public ResponseEntity<SpeciesResponse> criar(@Valid @RequestBody CriarSpeciesRequest request) {
        SpeciesResponse response = mapper.paraResponse(commandBus.dispatch(
                mapper.paraCommand(request, correlationKeyContext.atual())
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public SpeciesResponse buscarPorId(@PathVariable @Min(1) Long id) {
        return mapper.paraResponse(queryBus.dispatch(new BuscarSpeciesPorIdQuery(id)));
    }

    @GetMapping("/pokedex/{pokedexNumber}")
    public SpeciesResponse buscarPorPokedexNumber(@PathVariable @Min(1) Integer pokedexNumber) {
        return mapper.paraResponse(queryBus.dispatch(
                new BuscarSpeciesPorPokedexNumberQuery(pokedexNumber)
        ));
    }

    @GetMapping
    public PaginaResponse<SpeciesResponse> listar(
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20") @Min(1) int tamanho
    ) {
        return mapper.paraPaginaResponse(queryBus.dispatch(new ListarSpeciesQuery(pagina, tamanho)));
    }

    @PostMapping("/{speciesId}/moves")
    public ResponseEntity<SpeciesMoveResponse> adicionarMoveAoMoveset(
            @PathVariable @Min(1) Long speciesId,
            @Valid @RequestBody AdicionarMoveAoMovesetSpeciesRequest request
    ) {
        SpeciesMoveResponse response = speciesMoveApiMapper.paraResponse(commandBus.dispatch(
                speciesMoveApiMapper.paraCommand(speciesId, request, correlationKeyContext.atual())
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{speciesId}/moves/{moveId}")
    public ResponseEntity<Void> removerMoveDoMoveset(
            @PathVariable @Min(1) Long speciesId,
            @PathVariable @Min(1) Long moveId
    ) {
        commandBus.dispatch(new RemoverMoveDoMovesetSpeciesCommand(speciesId, moveId, correlationKeyContext.atual()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{speciesId}/moves")
    public List<MovesetSpeciesResponse> listarMoveset(@PathVariable @Min(1) Long speciesId) {
        return queryBus.<List<br.com.pokeidle3d.application.usecases.listarmovesetdaspecies.MovesetSpeciesItem>>dispatch(
                        new ListarMovesetDaSpeciesQuery(speciesId)
                )
                .stream()
                .map(speciesMoveApiMapper::paraMovesetResponse)
                .toList();
    }
}
