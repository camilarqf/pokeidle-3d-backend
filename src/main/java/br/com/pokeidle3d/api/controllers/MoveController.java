package br.com.pokeidle3d.api.controllers;

import br.com.pokeidle3d.api.contracts.CriarMoveRequest;
import br.com.pokeidle3d.api.contracts.MoveResponse;
import br.com.pokeidle3d.api.contracts.PaginaResponse;
import br.com.pokeidle3d.api.mappers.MoveApiMapper;
import br.com.pokeidle3d.application.usecases.buscarmoveporid.BuscarMovePorIdQuery;
import br.com.pokeidle3d.application.usecases.buscarmoveporname.BuscarMovePorNameQuery;
import br.com.pokeidle3d.application.usecases.listarmovesporcategory.ListarMovesPorCategoryQuery;
import br.com.pokeidle3d.application.usecases.listarmovesportype.ListarMovesPorTypeQuery;
import br.com.pokeidle3d.application.usecases.listarmoves.ListarMovesQuery;
import br.com.pokeidle3d.application.usecases.buscarmoveporid.BuscarMovePorIdUseCase;
import br.com.pokeidle3d.application.usecases.buscarmoveporname.BuscarMovePorNameUseCase;
import br.com.pokeidle3d.application.usecases.criarmove.CriarMoveUseCase;
import br.com.pokeidle3d.application.usecases.listarmovesporcategory.ListarMovesPorCategoryUseCase;
import br.com.pokeidle3d.application.usecases.listarmovesportype.ListarMovesPorTypeUseCase;
import br.com.pokeidle3d.application.usecases.listarmoves.ListarMovesUseCase;
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

@Validated
@RestController
@RequestMapping("/api/moves")
public class MoveController {

    private final CriarMoveUseCase criarMoveUseCase;
    private final BuscarMovePorIdUseCase buscarMovePorIdUseCase;
    private final BuscarMovePorNameUseCase buscarMovePorNameUseCase;
    private final ListarMovesUseCase listarMovesUseCase;
    private final ListarMovesPorTypeUseCase listarMovesPorTypeUseCase;
    private final ListarMovesPorCategoryUseCase listarMovesPorCategoryUseCase;
    private final MoveApiMapper mapper;

    public MoveController(
            CriarMoveUseCase criarMoveUseCase,
            BuscarMovePorIdUseCase buscarMovePorIdUseCase,
            BuscarMovePorNameUseCase buscarMovePorNameUseCase,
            ListarMovesUseCase listarMovesUseCase,
            ListarMovesPorTypeUseCase listarMovesPorTypeUseCase,
            ListarMovesPorCategoryUseCase listarMovesPorCategoryUseCase,
            MoveApiMapper mapper
    ) {
        this.criarMoveUseCase = criarMoveUseCase;
        this.buscarMovePorIdUseCase = buscarMovePorIdUseCase;
        this.buscarMovePorNameUseCase = buscarMovePorNameUseCase;
        this.listarMovesUseCase = listarMovesUseCase;
        this.listarMovesPorTypeUseCase = listarMovesPorTypeUseCase;
        this.listarMovesPorCategoryUseCase = listarMovesPorCategoryUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<MoveResponse> criar(@Valid @RequestBody CriarMoveRequest request) {
        MoveResponse response = mapper.paraResponse(criarMoveUseCase.handle(mapper.paraCommand(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public MoveResponse buscarPorId(@PathVariable @Min(1) Long id) {
        return mapper.paraResponse(buscarMovePorIdUseCase.handle(new BuscarMovePorIdQuery(id)));
    }

    @GetMapping("/name/{name}")
    public MoveResponse buscarPorName(@PathVariable String name) {
        return mapper.paraResponse(buscarMovePorNameUseCase.handle(new BuscarMovePorNameQuery(name)));
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
            return mapper.paraPaginaResponse(listarMovesPorTypeUseCase.handle(new ListarMovesPorTypeQuery(type, pagina, tamanho)));
        }
        if (category != null) {
            return mapper.paraPaginaResponse(listarMovesPorCategoryUseCase.handle(new ListarMovesPorCategoryQuery(category, pagina, tamanho)));
        }
        return mapper.paraPaginaResponse(listarMovesUseCase.handle(new ListarMovesQuery(pagina, tamanho)));
    }
}
