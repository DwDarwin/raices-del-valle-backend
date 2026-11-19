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

        Producto producto = convertirAEntidad(requestDTO);

        Producto productoGuardado = productoRepository.save(producto);

        return convertirAResponseDTO(productoGuardado);
    }

    @Override
    //@Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductos() {

        return productoRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
   // @Transactional(readOnly = true)
    public ProductoResponseDTO buscarProductoPorId(Integer id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con ID: " + id
                ));

        return convertirAResponseDTO(producto);
    }

    @Override
    //@Transactional(readOnly = true)
    public List<ProductoResponseDTO> filtrarPorCategoria(String categoria) {
        return productoRepository.findByCategoriaIgnoreCase(categoria)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }
    @Override
    //@Transactional(readOnly = true)
    public List<ProductoResponseDTO> ordenarPorMayorPrecio() {
        return productoRepository.findAllByOrderByPrecioDesc()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }
    @Override
    //@Transactional(readOnly = true)
    public List<ProductoResponseDTO> ordenarPorMenorPrecio() {
        return productoRepository.findAllByOrderByPrecioAsc()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public ProductoResponseDTO actualizarProducto(
            Integer id,
            ProductoRequestDTO requestDTO) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con ID: " + id
                ));

        producto.setNombre(requestDTO.getNombre());
        producto.setDescripcion(requestDTO.getDescripcion());
        producto.setCategoria(requestDTO.getCategoria());
        producto.setProductor(requestDTO.getProductor());
        producto.setPrecio(requestDTO.getPrecio());
        producto.setUnidadVenta(requestDTO.getUnidadVenta());
        producto.setStock(requestDTO.getStock());
        producto.setEstado(requestDTO.getEstado());
        producto.setImagen(requestDTO.getImagen());

        Producto productoActualizado = productoRepository.save(producto);

        return convertirAResponseDTO(productoActualizado);
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


    // Convertir RequestDTO a entidad
    private Producto convertirAEntidad(ProductoRequestDTO requestDTO) {

        Producto producto = new Producto();

        producto.setNombre(requestDTO.getNombre());
        producto.setDescripcion(requestDTO.getDescripcion());
        producto.setCategoria(requestDTO.getCategoria());
        producto.setProductor(requestDTO.getProductor());
        producto.setPrecio(requestDTO.getPrecio());
        producto.setUnidadVenta(requestDTO.getUnidadVenta());
        producto.setStock(requestDTO.getStock());
        producto.setEstado(requestDTO.getEstado());
        producto.setImagen(requestDTO.getImagen());

        return producto;
    }

    // Convertir entidad a ResponseDTO
    private ProductoResponseDTO convertirAResponseDTO(Producto producto) {

        ProductoResponseDTO responseDTO = new ProductoResponseDTO();

        responseDTO.setId(producto.getId());
        responseDTO.setNombre(producto.getNombre());
        responseDTO.setDescripcion(producto.getDescripcion());
        responseDTO.setCategoria(producto.getCategoria());
        responseDTO.setProductor(producto.getProductor());
        responseDTO.setPrecio(producto.getPrecio());
        responseDTO.setUnidadVenta(producto.getUnidadVenta());
        responseDTO.setStock(producto.getStock());
        responseDTO.setEstado(producto.getEstado());
        responseDTO.setImagen(producto.getImagen());

        return responseDTO;
    }
}
