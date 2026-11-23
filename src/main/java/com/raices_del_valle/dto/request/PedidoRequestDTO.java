package com.raices_del_valle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

    private Integer idUsuario;
    private BigDecimal descuento;
    private List<ItemPedidoRequestDTO> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedidoRequestDTO {
        private Integer idProducto;
        private Integer cantidad;
    }
}