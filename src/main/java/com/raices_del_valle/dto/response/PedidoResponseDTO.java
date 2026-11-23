package com.raices_del_valle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDTO {

    private Integer id;
    private String codigoPedido;
    private Integer idUsuario;
    private String nombreUsuario;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private LocalDateTime fechaPedido;
    private List<DetallePedidoResponseDTO> detalles;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetallePedidoResponseDTO {
        private Integer id;
        private Integer idProducto;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}