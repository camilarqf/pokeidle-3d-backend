package br.com.pokeidle3d.api.controllers;

import br.com.pokeidle3d.api.contracts.CriarMoveRequest;
import br.com.pokeidle3d.api.contracts.MoveResponse;
import br.com.pokeidle3d.api.contracts.PaginaResponse;
import br.com.pokeidle3d.api.contracts.SpeciesPorMoveResponse;
import br.com.pokeidle3d.api.mappers.MoveApiMapper;
import br.com.pokeidle3d.api.mappers.SpeciesMoveApiMapper;
import br.com.pokeidle3d.application.bus.CommandBus;
import br.com.pokeidle3d.application.bus.QueryBus;
import br.com.pokeidle3d.application.usecases.buscarmoveporid.BuscarMovePorIdQuery;
import br.com.pokeidle3d.application.usecases.buscarmoveporname.BuscarMovePorNameQuery;
import br.com.pokeidle3d.application.usecases.listarspeciespormove.ListarSpeciesPorMoveQuery;
import br.com.pokeidle3d.application.usecases.listarmovesporcategory.ListarMovesPorCategoryQuery;
import br.com.pokeidle3d.application.usecases.listarmovesportype.ListarMovesPorTypeQuery;
import br.com.pokeidle3d.application.usecases.listarmoves.ListarMovesQuery;
import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;
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
    public ResponseEntity<MoveResponse> criar(@Valid @RequestBody CriarMoveRequest request) {
        MoveResponse response = mapper.paraResponse(commandBus.dispatch(mapper.paraCommand(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public MoveResponse buscarPorId(@PathVariable @Min(1) Long id) {
        return mapper.paraResponse(queryBus.dispatch(new BuscarMovePorIdQuery(id)));
    }

    @GetMapping("/name/{name}")
    public MoveResponse buscarPorName(@PathVariable String name) {
        return mapper.paraResponse(queryBus.dispatch(new BuscarMovePorNameQuery(name)));
    }

    @GetMapping
    public PaginaResponse<MoveResponse> listar(
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20") @Min(1) int tamanho,
            @RequestParam(required = false) PokemonType type,
            @RequestParam(required = false) MoveCategory category
    ) {
        if (type != null && category != null) {
            throw new ValidacaoDominioException("Informe apenas type ou category");
        }
        if (type != null) {
            return mapper.paraPaginaResponse(queryBus.dispatch(new ListarMovesPorTypeQuery(type, pagina, tamanho)));
        }
        if (category != null) {
            return mapper.paraPaginaResponse(queryBus.dispatch(new ListarMovesPorCategoryQuery(category, pagina, tamanho)));
        }
        return mapper.paraPaginaResponse(queryBus.dispatch(new ListarMovesQuery(pagina, tamanho)));
    }

    @GetMapping("/{moveId}/species")
    public List<SpeciesPorMoveResponse> listarSpeciesPorMove(@PathVariable @Min(1) Long moveId) {
        return queryBus.<List<br.com.pokeidle3d.application.usecases.listarspeciespormove.SpeciesPorMoveItem>>dispatch(
                        new ListarSpeciesPorMoveQuery(moveId)
                )
                .stream()
                .map(speciesMoveApiMapper::paraSpeciesPorMoveResponse)
                .toList();
    }
}
