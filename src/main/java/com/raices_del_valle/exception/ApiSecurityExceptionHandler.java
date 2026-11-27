package com.raices_del_valle.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;

@Component
public class ApiSecurityExceptionHandler
        implements AuthenticationEntryPoint, AccessDeniedHandler {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException {

        escribirRespuesta(
                response,
                HttpStatus.UNAUTHORIZED,
                "No has iniciado sesión o tu sesión ha expirado."
        );
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception) throws IOException {

        escribirRespuesta(
                response,
                HttpStatus.FORBIDDEN,
                "No tienes permisos para realizar esta operación."
        );
    }

    private void escribirRespuesta(
            HttpServletResponse response,
            HttpStatus estado,
            String mensaje) throws IOException {

        response.setStatus(estado.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String timestamp = OffsetDateTime.now().toString();

        response.getWriter().write("""
                {
                    "status": %d,
                    "error": "%s",
                    "mensaje": "%s",
                    "timestamp": "%s"
                }
                """.formatted(
                estado.value(),
                estado.getReasonPhrase(),
                mensaje,
                timestamp
        ));
    }
}
