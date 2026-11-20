package com.raices_del_valle.service;

import com.raices_del_valle.dto.usuario.UsuarioActualizarDTO;
import com.raices_del_valle.dto.usuario.UsuarioLoginRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioRegistroDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;

public interface UsuarioService {

    UsuarioResponseDTO registrar(UsuarioRegistroDTO request);

    UsuarioResponseDTO login(UsuarioLoginRequestDTO request);

    UsuarioResponseDTO actualizarPerfil(Integer id, UsuarioActualizarDTO request);

    UsuarioResponseDTO obtenerPorId(Integer id);

    void desactivarCuenta(Integer id);
}
