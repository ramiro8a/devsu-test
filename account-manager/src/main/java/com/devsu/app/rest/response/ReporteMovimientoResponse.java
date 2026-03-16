package com.devsu.app.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReporteMovimientoResponse(
        LocalDateTime fechaMovimiento,
        String nroCuenta,
        Integer tipoCuenta,
        BigDecimal saldoInicial,
        Integer estadoCuenta,
        Integer tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo
) {
}
