package com.raices_del_valle.service.impl;

import com.raices_del_valle.dto.auth.LoginRequestDTO;
import com.raices_del_valle.dto.auth.RegistroRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;
import com.raices_del_valle.model.Usuario;
import com.raices_del_valle.repository.UsuarioRepository;
import com.raices_del_valle.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO registrar(RegistroRequestDTO request) {

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
    public UsuarioResponseDTO login(LoginRequestDTO request) {

        String correo = normalizarCorreo(request.getCorreo());

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));

        if (!Boolean.TRUE.equals(usuario.getEstado())) {
            throw new IllegalStateException("La cuenta está desactivada");
        }

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }
        return toResponse(usuario);
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
