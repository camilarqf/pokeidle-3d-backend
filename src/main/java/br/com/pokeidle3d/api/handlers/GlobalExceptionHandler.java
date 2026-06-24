package br.com.pokeidle3d.api.handlers;

import br.com.pokeidle3d.api.contracts.ErroResponse;
import br.com.pokeidle3d.domain.exceptions.MoveDuplicadoException;
import br.com.pokeidle3d.domain.exceptions.MoveNaoEncontradoException;
import br.com.pokeidle3d.domain.exceptions.SpeciesDuplicadaException;
import br.com.pokeidle3d.domain.exceptions.SpeciesNaoEncontradaException;
import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({SpeciesNaoEncontradaException.class, MoveNaoEncontradoException.class})
    public ResponseEntity<ErroResponse> tratarNaoEncontrada(RuntimeException exception, HttpServletRequest request) {
        return criarErro(HttpStatus.NOT_FOUND, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler({SpeciesDuplicadaException.class, MoveDuplicadoException.class})
    public ResponseEntity<ErroResponse> tratarDuplicada(RuntimeException exception, HttpServletRequest request) {
        return criarErro(HttpStatus.CONFLICT, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(ValidacaoDominioException.class)
    public ResponseEntity<ErroResponse> tratarValidacaoDominio(ValidacaoDominioException exception, HttpServletRequest request) {
        return criarErro(HttpStatus.BAD_REQUEST, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarPayloadInvalido(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<String> detalhes = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        return criarErro(HttpStatus.BAD_REQUEST, "Payload invalido", request, detalhes);
    }

    @ExceptionHandler({ConstraintViolationException.class, HandlerMethodValidationException.class})
    public ResponseEntity<ErroResponse> tratarParametroInvalido(Exception exception, HttpServletRequest request) {
        return criarErro(HttpStatus.BAD_REQUEST, "Parametro invalido", request, List.of(exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return criarErro(HttpStatus.BAD_REQUEST, "Payload invalido", request, List.of("JSON invalido ou valor incompativel"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tratarTipoParametroInvalido(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        return criarErro(HttpStatus.BAD_REQUEST, "Parametro invalido", request, List.of("Valor invalido para " + exception.getName()));
    }

    private ResponseEntity<ErroResponse> criarErro(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request,
            List<String> detalhes
    ) {
        ErroResponse response = new ErroResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI(),
                detalhes
        );
        return ResponseEntity.status(status).body(response);
    }
}
