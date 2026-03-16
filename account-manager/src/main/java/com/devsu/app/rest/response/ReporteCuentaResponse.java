package com.devsu.app.rest.response;

import java.math.BigDecimal;

public record ReporteCuentaResponse(
        Long cuentaId,
        String nroCuenta,
        Integer tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        Integer estado
) {
}
