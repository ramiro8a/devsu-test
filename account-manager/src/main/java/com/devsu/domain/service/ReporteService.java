package com.devsu.domain.service;

import com.devsu.app.rest.request.EstadoCuentaFilter;
import com.devsu.app.rest.response.ReporteEstadoCuentaResponse;

public interface ReporteService {
    ReporteEstadoCuentaResponse obtenerReporte(EstadoCuentaFilter filter);
}
