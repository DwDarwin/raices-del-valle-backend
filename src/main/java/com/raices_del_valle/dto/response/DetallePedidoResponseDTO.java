package com.raices_del_valle.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Lo que el backend le devuelve al frontend: datos planos, sin objetos Pedido ni Producto completos.
 */
@Getter
@Setter
@NoArgsConstructor
public class DetallePedidoResponseDTO {

    private Integer id;
    private Integer idPedido;
    private Integer idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}