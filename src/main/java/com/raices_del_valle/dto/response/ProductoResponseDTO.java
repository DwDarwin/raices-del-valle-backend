package com.raices_del_valle.dto.response;


import com.raices_del_valle.model.Producto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
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

    public static ProductoResponseDTO fromEntity(Producto producto) {

        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCategoria(producto.getCategoria());
        dto.setProductor(producto.getProductor());
        dto.setPrecio(producto.getPrecio());
        dto.setUnidadVenta(producto.getUnidadVenta());
        dto.setStock(producto.getStock());
        dto.setEstado(producto.getEstado());
        dto.setImagen(producto.getImagen());

        return dto;
    }
}