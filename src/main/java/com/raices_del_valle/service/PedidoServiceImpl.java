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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + request.getIdUsuario()));

        Pedido pedido = new Pedido();
        pedido.setCodigoPedido("PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now());

        BigDecimal subtotalGeneral = BigDecimal.ZERO;
        List<DetallePedido> detalles = new ArrayList<>();

        for (PedidoRequestDTO.ItemPedidoRequestDTO item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + item.getIdProducto()));

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotalItem = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));
            subtotalGeneral = subtotalGeneral.add(subtotalItem);

            // Actualizar stock del producto
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotalItem);

            detalles.add(detalle);
        }

        BigDecimal descuento = request.getDescuento() != null ? request.getDescuento() : BigDecimal.ZERO;
        BigDecimal total = subtotalGeneral.subtract(descuento).max(BigDecimal.ZERO);

        pedido.setSubtotal(subtotalGeneral);
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
        response.setTotal(pedido.getTotal());
        response.setFechaPedido(pedido.getFechaPedido());
        response.setDetalles(detallesDTO);

        return response;
    }
}