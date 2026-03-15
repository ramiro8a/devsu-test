package com.devsu.app.rest.response;

public record ClienteResponse(
        Long id,
        String nombres,
        String genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        Integer estado
) {
}
