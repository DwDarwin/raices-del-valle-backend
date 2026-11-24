package com.raices_del_valle.service;

import com.raices_del_valle.dto.auth.AuthResponseDTO;
import com.raices_del_valle.dto.auth.LoginRequestDTO;
import com.raices_del_valle.dto.auth.RegistroRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;

import com.raices_del_valle.dto.auth.RestablecerContrasenaRequestDTO;

public interface AuthService {
    
    UsuarioResponseDTO registrar(RegistroRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);


    void restablecerContrasena(
            RestablecerContrasenaRequestDTO request);
}
