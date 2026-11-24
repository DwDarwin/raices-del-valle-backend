package com.raices_del_valle.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestablecerContrasenaRequestDTO {

    @NotBlank(message = "El documento es obligatorio")
    private String documento;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String correo;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, max = 255,
            message = "La contraseña debe tener mínimo 8 caracteres")
    private String contrasenaNueva;
}