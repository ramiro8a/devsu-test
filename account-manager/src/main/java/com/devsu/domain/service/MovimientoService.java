package com.devsu.domain.service;

import com.devsu.app.rest.request.MovimientoRequest;
import com.devsu.app.rest.response.MovimientoResponse;

public interface MovimientoService {
    MovimientoResponse crear(MovimientoRequest request);
}
