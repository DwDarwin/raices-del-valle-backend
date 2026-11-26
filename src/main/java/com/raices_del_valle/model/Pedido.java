package com.raices_del_valle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * La compra. Versión mínima para que detalle_pedido compile y se pueda probar;
 * si el compañero de pedido sube la suya, se deja solo una.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_pedido", nullable = false, unique = true, length = 50)
    private String codigoPedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, precision = 13, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 13, scale = 2)
    private BigDecimal descuento;

    @Column(name = "costo_envio", nullable = false, precision = 13, scale = 2)
    private BigDecimal costoEnvio;

    @Column(nullable = false, precision = 13, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @PrePersist
    protected void alCrear() {
        if (this.fechaPedido == null) {
            this.fechaPedido = LocalDateTime.now();

        }
    }
}
