package com.raices_del_valle.service;

import com.raices_del_valle.dto.request.PedidoRequestDTO;
import com.raices_del_valle.dto.response.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {
    PedidoResponseDTO crearPedido(PedidoRequestDTO request);
    List<PedidoResponseDTO> listarPedidos();
    PedidoResponseDTO buscarPorId(Integer id);
    PedidoResponseDTO buscarPorCodigo(String codigoPedido);
    void eliminarPedido(Integer id);
}