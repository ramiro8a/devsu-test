package com.devsu.app.rest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record EstadoCuentaFilter(
        @NotNull(message = "La fecha de inicio no puede ser nula")
        LocalDate desde,
        @NotNull(message = "La fecha de fin no puede ser nula")
        LocalDate hasta,
        @NotNull(message = "El ID del cliente no puede ser nulo")
        @Positive(message = "El ID del cliente debe ser un número positivo")
        Long clientId
) {
}
