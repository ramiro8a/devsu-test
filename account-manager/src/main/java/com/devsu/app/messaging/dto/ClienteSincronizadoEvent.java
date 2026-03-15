package com.devsu.app.messaging.dto;

public record ClienteSincronizadoEvent(
        Long clienteId,
        String identificacion,
        String nombres,
        Integer estado,
        String tipoEvento
) {
}
