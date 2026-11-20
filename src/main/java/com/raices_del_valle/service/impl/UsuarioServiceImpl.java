package com.raices_del_valle.service.impl;

import com.raices_del_valle.dto.usuario.UsuarioActualizarDTO;
import com.raices_del_valle.dto.usuario.UsuarioLoginRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioRegistroDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;
import com.raices_del_valle.model.Usuario;
import com.raices_del_valle.repository.UsuarioRepository;
import com.raices_del_valle.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO registrar(UsuarioRegistroDTO request) {

        String correo = normalizarCorreo(request.getCorreo());

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException("El correo ya esta registrado");
        }
        if (usuarioRepository.existsByDocumento(request.getDocumento())) {
            throw new IllegalArgumentException("El documento de identidad ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .documento(request.getDocumento())
                .telefono(request.getTelefono())
                .correo(correo)
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return toResponse(usuarioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO login(UsuarioLoginRequestDTO request) {
        String correo = normalizarCorreo(request.getCorreo());
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }
        if (!Boolean.TRUE.equals(usuario.getEstado())) {
            throw new IllegalStateException("La cuenta no está activa");
        }
        return toResponse(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarPerfil(Integer id, UsuarioActualizarDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        String nuevoCorreo = normalizarCorreo(request.getCorreo());
        if (!usuario.getCorreo().equalsIgnoreCase(nuevoCorreo) && usuarioRepository.existsByCorreo(nuevoCorreo)) {
            throw new IllegalArgumentException("El nuevo correo ingresado ya está en uso");
        }

        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setCorreo(nuevoCorreo);

        if (request.getContrasenaNueva() != null && !request.getContrasenaNueva().isBlank()) {
            if (request.getContrasenaActual() == null || !passwordEncoder.matches(request.getContrasenaActual(), usuario.getContrasena())) {
                throw new IllegalArgumentException("La contrasena actual es incorrecta");
            }
            usuario.setContrasena(passwordEncoder.encode(request.getContrasenaNueva()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return toResponse(usuarioActualizado);
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
    public void desactivarCuenta(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuario.setEstado(false);
        usuarioRepository.save(usuario);
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
