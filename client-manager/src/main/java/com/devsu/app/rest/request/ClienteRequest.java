package com.devsu.app.rest.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
        String nombres,

        @NotBlank(message = "El género no puede estar vacío")
        @Pattern(regexp = "^[MFO]$", message = "El género debe ser 'M' o 'F' o 'O'")
        String genero,

        @NotNull(message = "La edad no puede ser nula")
        @Min(value = 0, message = "La edad no puede ser negativa")
        @Max(value = 60, message = "La edad no puede superar 60 años")
        Integer edad,

        @NotBlank(message = "La identificación no puede estar vacía")
        @Size(min = 5, max = 20, message = "La identificación debe tener entre 5 y 20 caracteres")
        //@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "La identificación solo puede contener letras y números")
        String identificacion,

        @NotBlank(message = "La dirección no puede estar vacía")
        String direccion,

        @NotBlank(message = "El teléfono no puede estar vacío")
        String telefono,

        @NotNull(message = "El estado no puede ser nulo")
        @Min(value = 0, message = "El estado debe ser 0 o 1")
        @Max(value = 1, message = "El estado debe ser 0 o 1")
        Integer estado,

        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
        String password
) {
}
