package br.com.pokeidle3d.api.controllers;

import br.com.pokeidle3d.api.contracts.CreateMoveRequest;
import br.com.pokeidle3d.api.contracts.MoveResponse;
import br.com.pokeidle3d.api.contracts.PageResponse;
import br.com.pokeidle3d.api.contracts.SpeciesByMoveResponse;
import br.com.pokeidle3d.api.mappers.MoveApiMapper;
import br.com.pokeidle3d.api.mappers.SpeciesMoveApiMapper;
import br.com.pokeidle3d.application.bus.CommandBus;
import br.com.pokeidle3d.application.bus.QueryBus;
import br.com.pokeidle3d.application.usecases.findmovebyid.FindMoveByIdQuery;
import br.com.pokeidle3d.application.usecases.findmovebyname.FindMoveByNameQuery;
import br.com.pokeidle3d.application.usecases.listspeciespormove.ListSpeciesByMoveQuery;
import br.com.pokeidle3d.application.usecases.listmovesporcategory.ListMovesByCategoryQuery;
import br.com.pokeidle3d.application.usecases.listmovesportype.ListMovesByTypeQuery;
import br.com.pokeidle3d.application.usecases.listmoves.ListMovesQuery;
import br.com.pokeidle3d.domain.exceptions.DomainValidationException;
import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;
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

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/moves")
public class MoveController {

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final MoveApiMapper mapper;
    private final SpeciesMoveApiMapper speciesMoveApiMapper;

    public MoveController(
            CommandBus commandBus,
            QueryBus queryBus,
            MoveApiMapper mapper,
            SpeciesMoveApiMapper speciesMoveApiMapper
    ) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.mapper = mapper;
        this.speciesMoveApiMapper = speciesMoveApiMapper;
    }

    @PostMapping
    public ResponseEntity<MoveResponse> criar(@Valid @RequestBody CreateMoveRequest request) {
        MoveResponse response = mapper.paraResponse(commandBus.dispatch(mapper.paraCommand(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public MoveResponse buscarPorId(@PathVariable @Min(1) Long id) {
        return mapper.paraResponse(queryBus.dispatch(new FindMoveByIdQuery(id)));
    }

    @GetMapping("/name/{name}")
    public MoveResponse buscarPorName(@PathVariable String name) {
        return mapper.paraResponse(queryBus.dispatch(new FindMoveByNameQuery(name)));
    }

    @GetMapping
    public PageResponse<MoveResponse> listar(
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20") @Min(1) int tamanho,
            @RequestParam(required = false) PokemonType type,
            @RequestParam(required = false) MoveCategory category
    ) {
        if (type != null && category != null) {
            throw new DomainValidationException("Informe apenas type ou category");
        }
        if (type != null) {
            return mapper.paraPageResponse(queryBus.dispatch(new ListMovesByTypeQuery(type, pagina, tamanho)));
        }
        if (category != null) {
            return mapper.paraPageResponse(queryBus.dispatch(new ListMovesByCategoryQuery(category, pagina, tamanho)));
        }
        return mapper.paraPageResponse(queryBus.dispatch(new ListMovesQuery(pagina, tamanho)));
    }

    @GetMapping("/{moveId}/species")
    public List<SpeciesByMoveResponse> listarSpeciesPorMove(@PathVariable @Min(1) Long moveId) {
        return queryBus.<List<br.com.pokeidle3d.application.usecases.listspeciespormove.SpeciesByMoveItem>>dispatch(
                        new ListSpeciesByMoveQuery(moveId)
                )
                .stream()
                .map(speciesMoveApiMapper::paraSpeciesByMoveResponse)
                .toList();
    }
}
