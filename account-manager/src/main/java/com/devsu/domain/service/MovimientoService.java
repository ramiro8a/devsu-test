package com.devsu.domain.service;

import com.devsu.app.rest.request.MovimientoRequest;
import com.devsu.app.rest.response.MovimientoResponse;

import java.util.List;

public interface MovimientoService {
    MovimientoResponse crear(MovimientoRequest request);
    List<MovimientoResponse> buscarPorNroCuenta(String nroCuenta);
}
