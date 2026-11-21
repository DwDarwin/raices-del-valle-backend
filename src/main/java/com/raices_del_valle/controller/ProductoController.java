package com.raices_del_valle.controller;


import com.raices_del_valle.dto.request.ProductoRequestDTO;
import com.raices_del_valle.dto.response.ProductoResponseDTO;
import com.raices_del_valle.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    //para ADMIN
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> guardarProducto(
            @RequestBody ProductoRequestDTO productoDTO) {

        ProductoResponseDTO productoGuardado =
                productoService.guardarProducto(productoDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoGuardado);
    }


    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {

        return ResponseEntity.ok(productoService.listarProductos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarProductoPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                productoService.buscarProductoPorId(id)
        );
    }
    //buscar por nombre - para catalogo
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(
            @RequestParam String nombre) {

        return ResponseEntity.ok(
                productoService.buscarPorNombre(nombre)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Integer id,
            @RequestBody ProductoRequestDTO productoDTO) {

        return ResponseEntity.ok(
                productoService.actualizarProducto(id, productoDTO)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProducto(
            @PathVariable Integer id) {

        productoService.eliminarProducto(id);

        //return ResponseEntity.noContent().build();
        return ResponseEntity.ok("El producto fue eliminado correctamente.");
    }


    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoResponseDTO>> filtrarPorCategoria(
            @PathVariable String categoria) {

        return ResponseEntity.ok(
                productoService.filtrarPorCategoria(categoria)
        );
    }


    @GetMapping("/mayor-precio")
    public ResponseEntity<List<ProductoResponseDTO>> ordenarPorMayorPrecio() {

        return ResponseEntity.ok(
                productoService.ordenarPorMayorPrecio()
        );
    }


    @GetMapping("/menor-precio")
    public ResponseEntity<List<ProductoResponseDTO>> ordenarPorMenorPrecio() {

        return ResponseEntity.ok(
                productoService.ordenarPorMenorPrecio()
        );
    }
}
