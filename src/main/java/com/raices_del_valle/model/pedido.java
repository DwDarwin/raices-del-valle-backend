package com.raices_del_valle.model;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import jakarta.persistence.*;


@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @Column(name = "codigo_pedido")
    private String codigoPedido;
    @ManyToOne
    @JoinColumn( name = "id_usuario")
    private Usuario usuario;
    @Column( name = "subtotal")
    private BigDecimal subtotal;
    @Column (name = "descueento")
    private BigDecimal descuento;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name= "fecha_pedido")
    private LocalDateTime fechaPedido;
}
