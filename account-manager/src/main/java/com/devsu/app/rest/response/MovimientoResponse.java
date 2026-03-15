package com.devsu.app.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoResponse(
        Long id,
        LocalDateTime fechaMovimiento,
        Integer tipoMovimiento,
        String tipoMovimientoDesc,
        BigDecimal valor,
        BigDecimal saldo
) {
}
