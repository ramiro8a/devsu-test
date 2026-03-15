package com.devsu.domain.provider.dto;

public record ClienteSincronizadoEvent(
        Long clienteId,
        String identificacion,
        String nombres,
        Integer estado,
        String tipoEvento
) {
}
