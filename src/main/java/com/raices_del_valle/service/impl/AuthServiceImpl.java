package com.raices_del_valle.service.impl;

import com.raices_del_valle.dto.auth.AuthResponseDTO;
import com.raices_del_valle.dto.auth.LoginRequestDTO;
import com.raices_del_valle.dto.auth.RegistroRequestDTO;
import com.raices_del_valle.dto.auth.RestablecerContrasenaRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;
import com.raices_del_valle.enums.Rol;
import com.raices_del_valle.model.Usuario;
import com.raices_del_valle.repository.UsuarioRepository;
import com.raices_del_valle.security.JwtService;
import com.raices_del_valle.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO registrar(RegistroRequestDTO request) {

        String correo = request.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new RuntimeException("El correo ya esta registrado");
        }
        if (usuarioRepository.existsByDocumento(request.getDocumento())) {
            throw new RuntimeException("El documento de identidad ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .documento(request.getDocumento())
                .telefono(request.getTelefono())
                .correo(correo)
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .rol(Rol.CLIENTE)
                .build();


        usuarioRepository.save(usuario);

        return toResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request) {

        String correo = request.getCorreo().trim().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        correo,
                        request.getContrasena()
                )
        );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        return AuthResponseDTO.builder()
                .token(token)
                .usuario(toResponse(usuario))
                .build();
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

    @Override
    public void restablecerContrasena(
            RestablecerContrasenaRequestDTO request) {

        Usuario usuario = usuarioRepository
                .findByDocumento(request.getDocumento())
                .orElseThrow(() ->
                        new RuntimeException("No se pudo restablecer la contraseña"));

        if (!usuario.getCorreo().equalsIgnoreCase(request.getCorreo())) {
            throw new RuntimeException("No se pudo restablecer la contraseña");
        }

        // Cifrar la nueva contraseña con BCrypt
        String nuevaContrasena = passwordEncoder.encode(
                request.getContrasenaNueva());

        usuario.setContrasena(nuevaContrasena);
        usuarioRepository.save(usuario);
    }


    /*
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

     */
}
