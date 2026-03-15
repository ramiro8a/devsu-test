package com.devsu.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoMovimiento {
    DEPOSITO(0, "Depósito"),
    RETIRO(1, "Retiro");
    private final Integer value;
    private final String description;

    public static String getDescription(Integer value) {
        for (TipoMovimiento status : values()) {
            if (status.getValue().equals(value)) {
                return status.getDescription();
            }
        }
        return "Desconocido";
    }
}
