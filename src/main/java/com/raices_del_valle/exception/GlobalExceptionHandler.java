package com.raices_del_valle.exception;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "Los datos enviados no son válidos.",
                errores
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(
            EntityNotFoundException ex) {

        return construirRespuesta(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarArgumentoInvalido(
            IllegalArgumentException ex) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(
            Exception ex) {

        return construirRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servidor. Inténtalo más tarde.",
                null
        );
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(
            HttpStatus estado,
            String mensaje,
            Object detalles) {

        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("status", estado.value());
        respuesta.put("error", estado.getReasonPhrase());
        respuesta.put("mensaje", mensaje);
        respuesta.put("detalles", detalles);
        respuesta.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(estado).body(respuesta);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> manejarCredencialesInvalidas(
            BadCredentialsException ex) {

        return construirRespuesta(
                HttpStatus.UNAUTHORIZED,
                "El correo o la contraseña son incorrectos.",
                null
        );
    }
}
