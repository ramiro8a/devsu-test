package com.devsu.app.rest.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CuentaUpdateRequest(
        @NotNull(message = "El tipo de cuenta no puede ser nulo")
        @Min(value = 0, message = "El tipo de cuenta debe estar entre 0(Ahorrro) y 1(Corriente)")
        @Max(value = 1, message = "El tipo de cuenta debe estar entre 0(Ahorrro) y 1(Corriente)")
        Integer tipoCuenta,
        @NotNull(message = "El estado no puede ser nulo")
        @Min(value = 0, message = "El tipo de cuenta debe estar entre 0(Activo) y 1(Inactivo)")
        @Max(value = 1, message = "El tipo de cuenta debe estar entre 0(Activo) y 1(Inactivo)")
        Integer estado
) {
}
