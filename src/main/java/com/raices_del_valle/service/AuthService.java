package com.raices_del_valle.service;

import com.raices_del_valle.dto.auth.LoginRequestDTO;
import com.raices_del_valle.dto.auth.RegistroRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;

public interface AuthService {
    
    UsuarioResponseDTO registrar(RegistroRequestDTO request);

    UsuarioResponseDTO login(LoginRequestDTO request);

}
