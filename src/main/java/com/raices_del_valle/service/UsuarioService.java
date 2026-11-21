package com.raices_del_valle.service;

import com.raices_del_valle.dto.usuario.CambiarContrasenaDTO;
import com.raices_del_valle.dto.usuario.UsuarioActualizarDTO;
import com.raices_del_valle.dto.usuario.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO actualizarPerfil(Integer id, UsuarioActualizarDTO request);

    void cambiarContrasena(Integer id, CambiarContrasenaDTO request);

    UsuarioResponseDTO obtenerPorId(Integer id);

    void desactivarCuenta(Integer id);

    List<UsuarioResponseDTO> listarUsuarios();

    UsuarioResponseDTO cambiarEstado(Integer id, Boolean estado);
}
