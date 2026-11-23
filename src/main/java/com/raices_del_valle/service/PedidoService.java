package com.raices_del_valle.service;

import com.raices_del_valle.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    List<Pedido> listarPedidos();

    Optional<Pedido> buscarPorId(Integer id);

    Optional<Pedido> buscarPorCodigoPedido(String codigoPedido);

    Pedido guardarPedido(Pedido pedido);

    void eliminarPedido(Integer id);
}