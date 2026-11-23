package com.raices_del_valle.controller;

import com.raices_del_valle.dto.request.DetallePedidoRequestDTO;
import com.raices_del_valle.dto.response.DetallePedidoResponseDTO;
import com.raices_del_valle.service.DetallePedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-pedido")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    // POST /api/detalles-pedido  -> agrega un producto a un pedido
    @PostMapping
    public ResponseEntity<DetallePedidoResponseDTO> crear(@RequestBody DetallePedidoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detallePedidoService.crear(request));
    }

    // GET /api/detalles-pedido  -> todas las líneas
    @GetMapping
    public ResponseEntity<List<DetallePedidoResponseDTO>> listar() {
        return ResponseEntity.ok(detallePedidoService.listar());
    }

    // GET /api/detalles-pedido/{id}  -> una línea
    @GetMapping("/{id}")
    public ResponseEntity<DetallePedidoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(detallePedidoService.buscarPorId(id));
    }

    // GET /api/detalles-pedido/pedido/{idPedido}  -> las líneas de una compra
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<DetallePedidoResponseDTO>> listarPorPedido(@PathVariable Integer idPedido) {
        return ResponseEntity.ok(detallePedidoService.listarPorPedido(idPedido));
    }

    // PUT /api/detalles-pedido/{id}  -> cambia la cantidad
    @PutMapping("/{id}")
    public ResponseEntity<DetallePedidoResponseDTO> actualizar(@PathVariable Integer id,
                                                               @RequestBody DetallePedidoRequestDTO request) {
        return ResponseEntity.ok(detallePedidoService.actualizar(id, request));
    }

    // DELETE /api/detalles-pedido/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        detallePedidoService.eliminar(id);
        return ResponseEntity.ok("El detalle de pedido fue eliminado correctamente.");
    }
}
