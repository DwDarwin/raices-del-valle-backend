package com.raices_del_valle.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 40)
    private String categoria;

    @Column(nullable = false, length = 120)
    private String productor;

    @Column(nullable = false, precision = 13, scale = 2)
    private BigDecimal precio;

    @Column(name = "unidad_venta", nullable = false, length = 60)
    private String unidadVenta;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(columnDefinition = "LONGTEXT")
    private String imagen;


    public Producto(Integer id, String nombre, String descripcion, String categoria,
                    String productor, BigDecimal precio, String unidadVenta, Integer stock,
                    String estado, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.productor = productor;
        this.precio = precio;
        this.unidadVenta = unidadVenta;
        this.stock = stock;
        this.estado = estado;
        this.imagen = imagen;
    }
}
