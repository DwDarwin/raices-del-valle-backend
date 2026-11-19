package com.raices_del_valle.dto.usuario;

import com.raices_del_valle.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Integer id;
    private String nombre;
    private String documento;
    private String telefono;
    private String correo;
    private Rol rol;
    private Boolean estado;
    private LocalDateTime fechaRegistro;
}
