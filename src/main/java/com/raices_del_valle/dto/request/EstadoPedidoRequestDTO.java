package com.raices_del_valle.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstadoPedidoRequestDTO {

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}