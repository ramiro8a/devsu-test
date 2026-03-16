package com.devsu.app.rest.response;

import java.time.LocalDate;
import java.util.List;

public record ReporteEstadoCuentaResponse(
        Long clienteId,
        String identificacion,
        String nombre,
        LocalDate desde,
        LocalDate hasta,
        List<ReporteCuentaResponse> cuentas,
        List<ReporteMovimientoResponse> movimientos
) {
}
