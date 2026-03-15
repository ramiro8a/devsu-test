package com.devsu.app.rest.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CuentaRequest(
        @NotNull(message = "El ID del cliente no puede ser nulo")
        @Positive(message = "El ID del cliente debe ser un número positivo")
        Long clientId,

        @NotNull(message = "El tipo de cuenta no puede ser nulo")
        @Min(value = 0, message = "El tipo de cuenta debe estar entre 0(Ahorrro) y 1(Corriente)")
        @Max(value = 1, message = "El tipo de cuenta debe estar entre 0(Ahorrro) y 1(Corriente)")
        Integer tipoCuenta,

        @NotNull(message = "El saldo no puede ser nulo")
        @DecimalMin(value = "0.00", inclusive = true, message = "El saldo no puede ser negativo")
        @Digits(integer = 12, fraction = 2, message = "El saldo debe tener máximo 12 dígitos enteros y 2 decimales")
        BigDecimal saldo,

        @NotNull(message = "El estado no puede ser nulo")
        @Min(value = 0, message = "El tipo de cuenta debe estar entre 0(Activo) y 1(Inactivo)")
        @Max(value = 1, message = "El tipo de cuenta debe estar entre 0(Activo) y 1(Inactivo)")
        Integer estado
) {
}
