package com.raices_del_valle.service;

import com.raices_del_valle.dto.request.ProductoRequestDTO;
import com.raices_del_valle.dto.response.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {

    ProductoResponseDTO guardarProducto(ProductoRequestDTO requestDTO);

    List<ProductoResponseDTO> listarProductos();

    ProductoResponseDTO buscarProductoPorId(Integer id);

    ProductoResponseDTO actualizarProducto(Integer id, ProductoRequestDTO requestDTO);

    void eliminarProducto(Integer id);


    List<ProductoResponseDTO> filtrarPorCategoria(String categoria);

    List<ProductoResponseDTO> ordenarPorMayorPrecio();

    List<ProductoResponseDTO> ordenarPorMenorPrecio();
}
