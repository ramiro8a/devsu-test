package com.devsu.domain.service;

import com.devsu.app.rest.request.CuentaRequest;
import com.devsu.app.rest.request.CuentaUpdateRequest;
import com.devsu.app.rest.response.CuentaResponse;
import com.devsu.domain.entity.Cuenta;

import java.util.List;

public interface CuentaService {
    CuentaResponse crear(CuentaRequest request);
    CuentaResponse actualizar(Long id, CuentaUpdateRequest request);
    CuentaResponse obtenerPorId(Long id);
    Cuenta recuperaPorId(Long id);
    Cuenta recuperaPorNroCuenta(String nroCuenta);
    List<CuentaResponse> obtenerTodos();
    Cuenta saveHelper(Cuenta cuenta);
}
