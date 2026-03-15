package com.devsu.app.rest.response;

import java.math.BigDecimal;
import java.util.List;

public record CuentaResponse(
        Long id,
        String nroCuenta,
        Integer tipoCuenta,
        String tipoCuentaDesc,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        Integer estado,
        String estadoDesc,
        List<MovimientoResponse> movimientos
) {
}
