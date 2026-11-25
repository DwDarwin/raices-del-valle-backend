
package com.raices_del_valle.config;


import com.raices_del_valle.enums.Rol;
import com.raices_del_valle.model.Usuario;
import com.raices_del_valle.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Value("${admin.correo}")
    private String correo;

    @Value("${admin.contrasena}")
    private String contrasena;

    @Bean
    public CommandLineRunner crearAdministrador(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (usuarioRepository.existsByCorreo(correo)) {
                System.out.println("La cuenta administradora ya existe.");
                return;
            }

            Usuario administrador = Usuario.builder()
                    .nombre("Administrador")
                    .documento("999999999")
                    .telefono("3001234567")
                    .correo(correo.trim().toLowerCase())
                    .contrasena(passwordEncoder.encode(contrasena))
                    .rol(Rol.ADMIN)
                    .estado(true)
                    .build();

            usuarioRepository.save(administrador);

            System.out.println("Cuenta administradora creada correctamente.");
        };
    }
}
