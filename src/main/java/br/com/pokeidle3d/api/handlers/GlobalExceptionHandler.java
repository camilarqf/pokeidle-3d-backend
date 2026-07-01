package br.com.pokeidle3d.api.handlers;

import br.com.pokeidle3d.api.context.CorrelationKeyContext;
import br.com.pokeidle3d.api.contracts.ErrorResponse;
import br.com.pokeidle3d.domain.exceptions.DuplicateMoveException;
import br.com.pokeidle3d.domain.exceptions.MoveNotFoundException;
import br.com.pokeidle3d.domain.exceptions.DuplicateSpeciesException;
import br.com.pokeidle3d.domain.exceptions.DuplicateSpeciesMoveException;
import br.com.pokeidle3d.domain.exceptions.SpeciesMoveNotFoundException;
import br.com.pokeidle3d.domain.exceptions.SpeciesNotFoundException;
import br.com.pokeidle3d.domain.exceptions.DomainValidationException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final CorrelationKeyContext correlationKeyContext;

    public GlobalExceptionHandler(CorrelationKeyContext correlationKeyContext) {
        this.correlationKeyContext = correlationKeyContext;
    }

    @ExceptionHandler({SpeciesNotFoundException.class, MoveNotFoundException.class, SpeciesMoveNotFoundException.class})
    public ResponseEntity<ErrorResponse> tratarNaoEncontrada(RuntimeException exception, HttpServletRequest request) {
        return criarErro(HttpStatus.NOT_FOUND, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler({DuplicateSpeciesException.class, DuplicateMoveException.class, DuplicateSpeciesMoveException.class})
    public ResponseEntity<ErrorResponse> tratarDuplicada(RuntimeException exception, HttpServletRequest request) {
        return criarErro(HttpStatus.CONFLICT, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponse> tratarValidacaoDominio(DomainValidationException exception, HttpServletRequest request) {
        return criarErro(HttpStatus.BAD_REQUEST, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> tratarPayloadInvalido(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<String> detalhes = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        return criarErro(HttpStatus.BAD_REQUEST, "Payload invalido", request, detalhes);
    }

    @ExceptionHandler({ConstraintViolationException.class, HandlerMethodValidationException.class})
    public ResponseEntity<ErrorResponse> tratarParametroInvalido(Exception exception, HttpServletRequest request) {
        return criarErro(HttpStatus.BAD_REQUEST, "Parametro invalido", request, List.of(exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> tratarJsonInvalido(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return criarErro(HttpStatus.BAD_REQUEST, "Payload invalido", request, List.of("JSON invalido ou valor incompativel"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> tratarTipoParametroInvalido(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        return criarErro(HttpStatus.BAD_REQUEST, "Parametro invalido", request, List.of("Valor invalido para " + exception.getName()));
    }

    private ResponseEntity<ErrorResponse> criarErro(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request,
            List<String> detalhes
    ) {
        String correlationKey = correlationKeyContext.atual().value();
        LOGGER.warn(
                "Erro tratado na API: status={}, path={}, correlationKey={}, mensagem={}",
                status.value(),
                request.getRequestURI(),
                correlationKey,
                mensagem
        );
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI(),
                correlationKey,
                detalhes
        );
        return ResponseEntity.status(status).body(response);
    }
}
