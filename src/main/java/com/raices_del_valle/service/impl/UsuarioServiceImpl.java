package com.raices_del_valle.service.impl;

import com.raices_del_valle.dto.usuario.CambiarContrasenaRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioActualizarRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;
import com.raices_del_valle.model.Usuario;
import com.raices_del_valle.repository.UsuarioRepository;
import com.raices_del_valle.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return toResponse(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarPerfil(Integer id, UsuarioActualizarRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        String nuevoCorreo = normalizarCorreo(request.getCorreo());
        if (!usuario.getCorreo().equalsIgnoreCase(nuevoCorreo) && usuarioRepository.existsByCorreo(nuevoCorreo)) {
            throw new IllegalArgumentException("El nuevo correo ingresado ya está en uso");
        }

        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setCorreo(nuevoCorreo);

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return toResponse(usuarioActualizado);
    }

    @Override
    @Transactional
    public void cambiarContrasena(Integer id, CambiarContrasenaRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        if (!passwordEncoder.matches(request.getContrasenaActual(), usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        if (passwordEncoder.matches(request.getContrasenaNueva(), usuario.getContrasena())) {
            throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
        }

        usuario.setContrasena(passwordEncoder.encode(request.getContrasenaNueva()));
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void desactivarCuenta(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuario.setEstado(false);
        usuarioRepository.save(usuario);
    }

    /**
     * Admin methods
     */

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioResponseDTO cambiarEstado(Integer id, Boolean estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        usuario.setEstado(estado);
        Usuario actualizado = usuarioRepository.save(usuario);
        return toResponse(actualizado);
    }

    private String normalizarCorreo(String correo) {
        return correo == null ? null : correo.trim().toLowerCase();
    }

    private UsuarioResponseDTO toResponse(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .documento(usuario.getDocumento())
                .telefono(usuario.getTelefono())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .estado(usuario.getEstado())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }
}
