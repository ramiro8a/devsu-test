package com.devsu.app.rest.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MovimientoRequest(
        @NotBlank(message = "El número de cuenta no puede estar vacío")
        String nroCuenta,
        @NotNull(message = "El tipo de movimiento no puede ser nulo")
        @Min(value = 0, message = "El tipo de movimiento debe estar en 0(Depósito) o 1(Retiro)")
        @Max(value = 1, message = "El tipo de movimiento debe estar en 0(Depósito) o 1(Retiro)")
        Integer tipoMovimiento,
        @NotNull(message = "El valor no puede ser nulo")
        BigDecimal valor
) {
}
