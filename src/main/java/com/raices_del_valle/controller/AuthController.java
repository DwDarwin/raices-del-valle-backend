package com.raices_del_valle.controller;

import com.raices_del_valle.dto.auth.AuthResponseDTO;
import com.raices_del_valle.dto.auth.LoginRequestDTO;
import com.raices_del_valle.dto.auth.RegistroRequestDTO;
import com.raices_del_valle.dto.auth.RestablecerContrasenaRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;
import com.raices_del_valle.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        UsuarioResponseDTO response = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO token = authService.login(request);
        return ResponseEntity.ok(token);
    }


    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<String> restablecerContrasena(
            @Valid @RequestBody RestablecerContrasenaRequestDTO request) {

        authService.restablecerContrasena(request);

        return ResponseEntity.ok(
                "La contraseña se ha restablecido correctamente");
    }
}
