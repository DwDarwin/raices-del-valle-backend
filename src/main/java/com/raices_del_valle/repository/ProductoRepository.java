package com.raices_del_valle.repository;

import com.raices_del_valle.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {


    List<Producto> findByCategoriaIgnoreCase(String categoria);
    List<Producto> findAllByOrderByPrecioDesc();
    List<Producto> findAllByOrderByPrecioAsc();
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

}
