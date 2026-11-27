package com.raices_del_valle.service;

import com.raices_del_valle.dto.request.PedidoRequestDTO;
import com.raices_del_valle.dto.response.PedidoResponseDTO;
import com.raices_del_valle.model.DetallePedido;
import com.raices_del_valle.model.Pedido;
import com.raices_del_valle.model.Producto;
import com.raices_del_valle.model.Usuario;
import com.raices_del_valle.repository.DetallePedidoRepository;
import com.raices_del_valle.repository.PedidoRepository;
import com.raices_del_valle.repository.ProductoRepository;
import com.raices_del_valle.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    private static final BigDecimal COSTO_ENVIO_BASE =
            new BigDecimal("14000");

    @Override
    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud del pedido no puede ser nula");
        }

        if (request.getIdUsuario() == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }

        // Impedir pedidos sin productos
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un producto");
        }

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + request.getIdUsuario()));

        Pedido pedido = new Pedido();
        pedido.setCodigoPedido("PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now());

        BigDecimal subtotalGeneral = BigDecimal.ZERO;
        List<DetallePedido> detalles = new ArrayList<>();
        Set<Integer> productosVistos = new HashSet<>();

        for (PedidoRequestDTO.ItemPedidoRequestDTO item : request.getItems()) {
            if (item == null || item.getIdProducto() == null) {
                throw new IllegalArgumentException("Cada ítem del pedido debe tener un ID de producto válido");
            }

            // 2. Evitar productos duplicados en el mismo pedido
            if (!productosVistos.add(item.getIdProducto())) {
                throw new IllegalArgumentException(
                        "El producto con ID " + item.getIdProducto() + " está duplicado en el pedido"
                );
            }

            // Impedir cantidades iguales o inferiores a cero
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad del producto debe ser mayor a cero");
            }

            Producto producto = productoRepository.findByIdAndActivoTrue(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + item.getIdProducto()));

            // 1. Validar que el producto esté PUBLICADO
            if (producto.getEstado() == null || !"PUBLICADO".equalsIgnoreCase(producto.getEstado().trim())) {
                throw new IllegalArgumentException(
                        "El producto '" + producto.getNombre() + "' no está disponible para la venta (estado: " + producto.getEstado() + ")"
                );
            }

            // 4. Validar stock no nulo y precio válido en BD
            if (producto.getStock() == null) {
                throw new IllegalStateException(
                        "El producto '" + producto.getNombre() + "' no tiene un stock configurado"
                );
            }

            if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException(
                        "El producto '" + producto.getNombre() + "' no tiene un precio válido"
                );
            }

            if (producto.getStock() < item.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotalItem = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));
            subtotalGeneral = subtotalGeneral.add(subtotalItem);

            // Actualizar stock del producto y marcar AGOTADO si llega a 0
            int nuevoStock = producto.getStock() - item.getCantidad();
            producto.setStock(nuevoStock);
            if (nuevoStock == 0) {
                producto.setEstado("AGOTADO");
            }
            productoRepository.save(producto);

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotalItem);

            detalles.add(detalle);
        }

        // Validar que el descuento no sea negativo ni superior al subtotal
        BigDecimal descuento = request.getDescuento() != null
                ? request.getDescuento()
                : BigDecimal.ZERO;

        if (descuento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo");
        }

        if (descuento.compareTo(subtotalGeneral) > 0) {
            throw new IllegalArgumentException("El descuento no puede ser superior al subtotal del pedido");
        }

        /* calculo del pedido incluyendo el costo de envío base */
        BigDecimal costoEnvio = COSTO_ENVIO_BASE;

        BigDecimal total = subtotalGeneral
                .subtract(descuento)
                .add(costoEnvio);

        pedido.setSubtotal(subtotalGeneral);
        pedido.setCostoEnvio(costoEnvio);
        pedido.setDescuento(descuento);
        pedido.setTotal(total);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        List<DetallePedido> detallesGuardados = detallePedidoRepository.saveAll(detalles);

        return mapearAResponse(pedidoGuardado, detallesGuardados);
    }

    @Override
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll().stream()
                .map(pedido -> {
                    List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
                    return mapearAResponse(pedido, detalles);
                })
                .toList();
    }

    @Override
    public PedidoResponseDTO buscarPorId(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
        return mapearAResponse(pedido, detalles);
    }

    @Override
    public PedidoResponseDTO buscarPorCodigo(String codigoPedido) {
        Pedido pedido = pedidoRepository.findByCodigoPedido(codigoPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con código: " + codigoPedido));
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
        return mapearAResponse(pedido, detalles);
    }

    @Override
    @Transactional
    public void eliminarPedido(Integer id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(id);
        detallePedidoRepository.deleteAll(detalles);
        pedidoRepository.deleteById(id);
    }

    private PedidoResponseDTO mapearAResponse(Pedido pedido, List<DetallePedido> detalles) {
        List<PedidoResponseDTO.DetallePedidoResponseDTO> detallesDTO = detalles.stream()
                .map(detalle -> PedidoResponseDTO.DetallePedidoResponseDTO.builder()
                        .id(detalle.getId())
                        .idProducto(detalle.getProducto().getId())
                        .nombreProducto(detalle.getProducto().getNombre())
                        .cantidad(detalle.getCantidad())
                        .precioUnitario(detalle.getPrecioUnitario())
                        .subtotal(detalle.getSubtotal())
                        .build())
                .toList();

        PedidoResponseDTO response = new PedidoResponseDTO();
        response.setId(pedido.getId());
        response.setCodigoPedido(pedido.getCodigoPedido());
        response.setIdUsuario(pedido.getUsuario().getId());
        response.setNombreUsuario(pedido.getUsuario().getNombre());
        response.setSubtotal(pedido.getSubtotal());
        response.setDescuento(pedido.getDescuento());
        response.setCostoEnvio(pedido.getCostoEnvio());
        response.setTotal(pedido.getTotal());
        response.setFechaPedido(pedido.getFechaPedido());
        response.setDetalles(detallesDTO);

        return response;
    }
}