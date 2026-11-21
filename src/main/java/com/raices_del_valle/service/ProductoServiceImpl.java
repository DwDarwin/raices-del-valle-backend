package com.raices_del_valle.service;

import com.raices_del_valle.dto.request.ProductoRequestDTO;
import com.raices_del_valle.dto.response.ProductoResponseDTO;
import com.raices_del_valle.model.Producto;
import com.raices_del_valle.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;


    @Override
    public ProductoResponseDTO guardarProducto(ProductoRequestDTO requestDTO) {

        Producto producto = requestDTO.toEntity();

        Producto productoGuardado = productoRepository.save(producto);

        return ProductoResponseDTO.fromEntity(productoGuardado);
    }


    @Override
    public List<ProductoResponseDTO> listarProductos() {

        return productoRepository.findAll()
                .stream()
                .map(ProductoResponseDTO::fromEntity)
                .toList();
    }


    @Override
    public ProductoResponseDTO buscarProductoPorId(Integer id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con ID: " + id
                ));

        return ProductoResponseDTO.fromEntity(producto);
    }
    @Override
    public List<ProductoResponseDTO> buscarPorNombre(String nombre) {

        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(ProductoResponseDTO::fromEntity)
                .toList();
    }


    @Override
    public List<ProductoResponseDTO> filtrarPorCategoria(String categoria) {

        return productoRepository.findByCategoriaIgnoreCase(categoria)
                .stream()
                .map(ProductoResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public List<ProductoResponseDTO> ordenarPorMayorPrecio() {

        return productoRepository.findAllByOrderByPrecioDesc()
                .stream()
                .map(ProductoResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public List<ProductoResponseDTO> ordenarPorMenorPrecio() {

        return productoRepository.findAllByOrderByPrecioAsc()
                .stream()
                .map(ProductoResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public ProductoResponseDTO actualizarProducto(
            Integer id,
            ProductoRequestDTO requestDTO) {

        productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con ID: " + id
                ));

        Producto producto = requestDTO.toEntity();
        producto.setId(id);

        Producto productoActualizado = productoRepository.save(producto);

        return ProductoResponseDTO.fromEntity(productoActualizado);
    }

    @Override
    public void eliminarProducto(Integer id) {

        if (!productoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Producto no encontrado con ID: " + id
            );
        }

        productoRepository.deleteById(id);
    }
}