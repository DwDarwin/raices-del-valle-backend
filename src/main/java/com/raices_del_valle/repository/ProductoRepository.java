package com.raices_del_valle.repository;

import com.raices_del_valle.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {


    // Solo productos activos (los eliminados lógicamente quedan ocultos)
    List<Producto> findByActivoTrue();
    Optional<Producto> findByIdAndActivoTrue(Integer id);
    List<Producto> findByCategoriaIgnoreCaseAndActivoTrue(String categoria);
    List<Producto> findByActivoTrueOrderByPrecioDesc();
    List<Producto> findByActivoTrueOrderByPrecioAsc();
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

}
