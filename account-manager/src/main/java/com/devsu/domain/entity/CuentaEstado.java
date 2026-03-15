package com.devsu.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CuentaEstado {
    ACTIVO(0, "Activo"),
    INACTIVO(1, "Inactivo");
    private final Integer value;
    private final String description;

    public static String getDescription(Integer value) {
        for (CuentaEstado status : values()) {
            if (status.getValue().equals(value)) {
                return status.getDescription();
            }
        }
        return "Desconocido";
    }
}
