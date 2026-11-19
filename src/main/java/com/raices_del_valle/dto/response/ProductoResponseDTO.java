package com.raices_del_valle.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ProductoResponseDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String productor;
    private BigDecimal precio;
    private String unidadVenta;
    private Integer stock;
    private String estado;
    private String imagen;
}
