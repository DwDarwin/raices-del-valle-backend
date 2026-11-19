package com.raices_del_valle.repository;

import com.raices_del_valle.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);

    Boolean existsByCorreo(String correo);

    Boolean existsByDocumento(String documento);

}
