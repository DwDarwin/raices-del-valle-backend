package com.raices_del_valle.service;

import com.raices_del_valle.dto.usuario.CambiarContrasenaRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioActualizarRequestDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO actualizarPerfil(Integer id, UsuarioActualizarRequestDTO request);

    void cambiarContrasena(Integer id, CambiarContrasenaRequestDTO request);

    UsuarioResponseDTO obtenerPorId(Integer id);

    void desactivarCuenta(Integer id);

    List<UsuarioResponseDTO> listarUsuarios();

    UsuarioResponseDTO cambiarEstado(Integer id, Boolean estado);
}
