package com.raices_del_valle.dto.request;

import com.raices_del_valle.model.Producto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ProductoRequestDTO {

    private String nombre;
    private String descripcion;
    private String categoria;
    private String productor;
    private BigDecimal precio;
    private String unidadVenta;
    private Integer stock;
    private String estado;
    private String imagen;

    public Producto toEntity() {

        Producto producto = new Producto();

        producto.setNombre(this.nombre);
        producto.setDescripcion(this.descripcion);
        producto.setCategoria(this.categoria);
        producto.setProductor(this.productor);
        producto.setPrecio(this.precio);
        producto.setUnidadVenta(this.unidadVenta);
        producto.setStock(this.stock);
        producto.setEstado(this.estado);
        producto.setImagen(this.imagen);

        return producto;
    }

}
