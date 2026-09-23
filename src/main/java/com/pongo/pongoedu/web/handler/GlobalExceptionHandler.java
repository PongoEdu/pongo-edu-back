package com.pongo.pongoedu.web.handler;

import com.pongo.pongoedu.domain.exception.ConflitoException;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.info("Recurso nao encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<Map<String, Object>> handleConflitoException(ConflitoException ex) {
        log.info("Conflito: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomainException(DomainException ex) {
        log.info("Erro de dominio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        var erros = new LinkedHashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(erro ->
                erros.put(erro.getField(), erro.getDefaultMessage()));

        var response = buildErrorResponse("Erro de validacao", HttpStatus.BAD_REQUEST);
        response.put("campos", erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        log.error("Erro interno: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Map<String, Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return response;
    }
}
