package com.raices_del_valle.repository;

import com.raices_del_valle.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JpaRepository ya trae el CRUD completo:
 *   save(...)        -> crear y actualizar
 *   findAll()        -> listar
 *   findById(id)     -> buscar uno
 *   deleteById(id)   -> eliminar
 *   existsById(id), count()
 * Abajo, consultas propias del detalle que Spring arma a partir del nombre del método.
 */
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    // Todas las líneas de una compra
    List<DetallePedido> findByPedidoId(Integer idPedido);

    // En qué compras aparece un producto
    List<DetallePedido> findByProductoId(Integer idProducto);

    // Para no repetir el mismo producto dos veces en una compra
    boolean existsByPedidoIdAndProductoId(Integer idPedido, Integer idProducto);
}
