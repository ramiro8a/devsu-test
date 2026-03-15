package com.devsu.domain.service.impl;

import com.devsu.app.rest.request.CuentaRequest;
import com.devsu.app.rest.request.CuentaUpdateRequest;
import com.devsu.app.rest.response.CuentaResponse;
import com.devsu.commons.enums.ErrorCode;
import com.devsu.commons.exceptions.AppException;
import com.devsu.domain.entity.ClienteRef;
import com.devsu.domain.entity.Cuenta;
import com.devsu.domain.entity.Movimiento;
import com.devsu.domain.repository.CuentaRepository;
import com.devsu.domain.service.ClienteRefService;
import com.devsu.domain.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CuentaServiceImpl implements CuentaService {
    private final CuentaRepository cuentaRepository;
    private final ClienteRefService clienteRefService;

    @Override
    public CuentaResponse crear(CuentaRequest request) {
        ClienteRef clienteRef = clienteRefService.buscaPorId(request.clientId());
        Cuenta cuenta = Cuenta.builder()
                .cliente(clienteRef)
                .nroCuenta(UUID.randomUUID().toString())
                .tipoCuenta(request.tipoCuenta())
                .saldoInicial(request.saldo())
                .saldoDisponible(request.saldo())
                .estado(request.estado())
                .build();
        if (request.saldo().compareTo(BigDecimal.ZERO) > 0) {
            Movimiento movimiento = Movimiento.builder()
                    .cuenta(cuenta)
                    .fechaMovimiento(LocalDateTime.now())
                    .tipoMovimiento(0)
                    .valor(request.saldo())
                    .saldo(request.saldo())
                    .build();
            cuenta.setHistorico(List.of(movimiento));
        }
        cuenta = cuentaRepository.save(cuenta);
        return cuenta.toResponse();
    }

    @Override
    public CuentaResponse actualizar(Long id, CuentaUpdateRequest request) {
        Cuenta cuenta = recuperaPorId(id);
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setEstado(request.estado());
        return (cuentaRepository.save(cuenta)).toResponse();
    }

    @Override
    public CuentaResponse obtenerPorId(Long id) {
        return (recuperaPorId(id)).toResponse();
    }

    @Override
    public Cuenta recuperaPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NO_EXISTE_CUENTA));
    }

    @Override
    public Cuenta recuperaPorNroCuenta(String nroCuenta) {
        return cuentaRepository.findByNroCuenta(nroCuenta)
                .orElseThrow(() -> new AppException(ErrorCode.NO_EXISTE_CUENTA));
    }

    @Override
    public List<CuentaResponse> obtenerTodos() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        return cuentas.stream().map(Cuenta::toResponse).toList();
    }

    @Override
    public Cuenta saveHelper(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }
}
