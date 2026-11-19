package com.raices_del_valle.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    private String nombre;

    @NotBlank(message = "El documento es obligatorio")
    @Size(min = 8, max = 45, message = "El documento debe tener entre 8 y 45 caracteres")
    private String documento;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(min = 7, max = 15, message = "El telefono debe tener entre 7 y 15 caracteres")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo electronico no es valido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    private String correo;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, max = 255, message = "La contrasena debe tener al menos 6 caracteres")
    private String contrasena;

}
