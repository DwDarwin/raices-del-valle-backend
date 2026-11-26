package com.raices_del_valle.service;

import com.raices_del_valle.dto.request.DetallePedidoRequestDTO;
import com.raices_del_valle.dto.response.DetallePedidoResponseDTO;
import com.raices_del_valle.model.DetallePedido;
import com.raices_del_valle.model.Pedido;
import com.raices_del_valle.model.Producto;
import com.raices_del_valle.repository.DetallePedidoRepository;
import com.raices_del_valle.repository.PedidoRepository;
import com.raices_del_valle.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementación del contrato DetallePedidoService: aquí está la lógica del CRUD.
 */
@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public DetallePedidoServiceImpl(DetallePedidoRepository detallePedidoRepository,
                                    PedidoRepository pedidoRepository,
                                    ProductoRepository productoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    // CREATE
    @Override
    @Transactional
    public DetallePedidoResponseDTO crear(DetallePedidoRequestDTO request) {
        validarCantidad(request.getCantidad());
        Pedido pedido = pedidoRepository.findById(request.getIdPedido())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el pedido " + request.getIdPedido()));
        Producto producto = productoRepository.findByIdAndActivoTrue(request.getIdProducto())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el producto " + request.getIdProducto()));

        if (detallePedidoRepository.existsByPedidoIdAndProductoId(pedido.getId(), producto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ese producto ya está en el pedido; actualiza la cantidad");
        }

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(producto.getPrecio());   // precio del momento de la compra
        detalle.setSubtotal(calcularSubtotal(detalle));

        return convertirAResponseDTO(detallePedidoRepository.save(detalle));
    }

    // READ: todos
    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoResponseDTO> listar() {
        return detallePedidoRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // READ: uno
    @Override
    @Transactional(readOnly = true)
    public DetallePedidoResponseDTO buscarPorId(Integer id) {
        return convertirAResponseDTO(obtenerEntidad(id));
    }

    // READ: las líneas de una compra
    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoResponseDTO> listarPorPedido(Integer idPedido) {
        return detallePedidoRepository.findByPedidoId(idPedido).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // UPDATE: solo cambia la cantidad; el precio de la compra se conserva
    @Override
    @Transactional
    public DetallePedidoResponseDTO actualizar(Integer id, DetallePedidoRequestDTO request) {
        validarCantidad(request.getCantidad());
        DetallePedido existente = obtenerEntidad(id);
        existente.setCantidad(request.getCantidad());
        existente.setSubtotal(calcularSubtotal(existente));
        return convertirAResponseDTO(detallePedidoRepository.save(existente));
    }

    // DELETE
    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!detallePedidoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el detalle de pedido " + id);
        }
        detallePedidoRepository.deleteById(id);
    }

    private DetallePedido obtenerEntidad(Integer id) {
        return detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el detalle de pedido " + id));
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor que 0");
        }
    }

    private BigDecimal calcularSubtotal(DetallePedido detalle) {
        return detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
    }

    // Convertir entidad a ResponseDTO
    private DetallePedidoResponseDTO convertirAResponseDTO(DetallePedido detalle) {
        DetallePedidoResponseDTO responseDTO = new DetallePedidoResponseDTO();

        responseDTO.setId(detalle.getId());
        responseDTO.setIdPedido(detalle.getPedido().getId());
        responseDTO.setIdProducto(detalle.getProducto().getId());
        responseDTO.setNombreProducto(detalle.getProducto().getNombre());
        responseDTO.setCantidad(detalle.getCantidad());
        responseDTO.setPrecioUnitario(detalle.getPrecioUnitario());
        responseDTO.setSubtotal(detalle.getSubtotal());

        return responseDTO;
    }
}
