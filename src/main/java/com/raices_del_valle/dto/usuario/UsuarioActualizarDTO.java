package com.raices_del_valle.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioActualizarDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    private String nombre;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(min = 7, max = 15, message = "El telefono debe tener entre 7 y 15 caracteres")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo electronico no es valido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    private String correo;

    private String contrasenaActual;

    @Pattern(
            regexp = "^$|.{6,255}",
            message = "La nueva contrasena debe tener entre 6 y 255 caracteres"
    )
    private String contrasenaNueva;

}
