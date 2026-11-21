package com.raices_del_valle.controller;

import com.raices_del_valle.dto.usuario.ActualizarPerfilRequestDTO;
import com.raices_del_valle.dto.usuario.CambiarContrasenaRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;
import com.raices_del_valle.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarPerfil(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarPerfilRequestDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(id, request));
    }

    @PatchMapping("/{id}/contrasena")
    public ResponseEntity<Void> cambiarContrasena(
            @PathVariable Integer id,
            @Valid @RequestBody CambiarContrasenaRequestDTO request) {
        usuarioService.cambiarContrasena(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarCuenta(@PathVariable Integer id) {
        usuarioService.desactivarCuenta(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * Admin endpoints
     */

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam Boolean estado) {
        return ResponseEntity.ok(usuarioService.cambiarEstado(id, estado));
    }
}
