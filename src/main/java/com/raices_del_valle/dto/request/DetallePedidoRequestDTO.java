package com.raices_del_valle.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lo que llega del frontend para crear o actualizar una línea de compra.
 * No trae precio ni subtotal: el precio se toma del producto y el subtotal lo calcula el backend.
 */
@Getter
@Setter
@NoArgsConstructor
public class DetallePedidoRequestDTO {

    private Integer idPedido;
    private Integer idProducto;
    private Integer cantidad;
}