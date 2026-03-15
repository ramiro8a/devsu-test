package com.devsu.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoCuenta {
    AHORRRO(0, "Ahorro"),
    CORRIENTE(1, "Corriente");
    private final Integer value;
    private final String description;

    public static String getDescription(Integer value) {
        for (TipoCuenta status : values()) {
            if (status.getValue().equals(value)) {
                return status.getDescription();
            }
        }
        return "Desconocido";
    }
}
