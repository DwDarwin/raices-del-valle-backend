package com.raices_del_valle.service;

import com.raices_del_valle.dto.request.DetallePedidoRequestDTO;
import com.raices_del_valle.dto.response.DetallePedidoResponseDTO;

import java.util.List;

/**
 * Contrato del servicio: qué operaciones existen sobre el detalle de pedido.
 * La lógica está en DetallePedidoServiceImpl.
 */
public interface DetallePedidoService {

    DetallePedidoResponseDTO crear(DetallePedidoRequestDTO request);

    List<DetallePedidoResponseDTO> listar();

    DetallePedidoResponseDTO buscarPorId(Integer id);

    List<DetallePedidoResponseDTO> listarPorPedido(Integer idPedido);

    DetallePedidoResponseDTO actualizar(Integer id, DetallePedidoRequestDTO request);

    void eliminar(Integer id);
}