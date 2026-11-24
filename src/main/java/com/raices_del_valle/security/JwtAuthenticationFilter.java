package com.raices_del_valle.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtener el encabezado Authorization
        final String authHeader = request.getHeader("Authorization");

        // 2. Verificar si existe un token Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token
        final String token = authHeader.substring(7);

        // 4. Obtener el correo del token
        final String correo;

        try {
            correo = jwtService.extractUsername(token);
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Comprobar si el usuario todavía no está autenticado
        if (correo != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Buscar el usuario en la base de datos
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(correo);

            // 7. Validar el token
            if (jwtService.isTokenValid(token, userDetails)) {

                // 8. Crear la autenticación
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // 9. Registrar la autenticación en Spring Security
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        // 10. Continuar con la siguiente etapa de la petición
        filterChain.doFilter(request, response);
    }
}