package com.devsu.domain.service.impl;

import com.devsu.app.rest.request.MovimientoRequest;
import com.devsu.app.rest.response.MovimientoResponse;
import com.devsu.commons.enums.ErrorCode;
import com.devsu.commons.exceptions.AppException;
import com.devsu.domain.entity.Cuenta;
import com.devsu.domain.entity.Movimiento;
import com.devsu.domain.entity.TipoMovimiento;
import com.devsu.domain.repository.MovimientoRepository;
import com.devsu.domain.service.CuentaService;
import com.devsu.domain.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovimientoServiceImpl implements MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final CuentaService cuentaService;

    @Override
    public MovimientoResponse crear(MovimientoRequest request) {
        BigDecimal valorMovimiento = request.valor();
        if (valorMovimiento.compareTo(BigDecimal.ZERO) == 0) {
            throw new AppException(ErrorCode.VALOR_MOVIMIENTO_INVALIDO);
        }
        if (valorMovimiento.compareTo(BigDecimal.ZERO) < 0) {
            valorMovimiento = valorMovimiento.abs();
        }
        Cuenta cuenta = cuentaService.recuperaPorNroCuenta(request.nroCuenta());
        Movimiento movimiento = Movimiento.builder()
                .cuenta(cuenta)
                .tipoMovimiento(request.tipoMovimiento())
                .valor(valorMovimiento)
                .fechaMovimiento(LocalDateTime.now())
                .build();
        if (TipoMovimiento.RETIRO.getValue().equals(request.tipoMovimiento())) {
            if(cuenta.getSaldoDisponible().compareTo(valorMovimiento) < 0){
                throw new AppException(ErrorCode.SALDO_INSUFICIENTE);
            }
            BigDecimal saldo = cuenta.getSaldoDisponible().subtract(valorMovimiento);
            movimiento.setSaldo(saldo);
            cuenta.setSaldoDisponible(saldo);
        } else if(TipoMovimiento.DEPOSITO.getValue().equals(request.tipoMovimiento())){
            BigDecimal saldo = cuenta.getSaldoDisponible().add(valorMovimiento);
            movimiento.setSaldo(saldo);
            cuenta.setSaldoDisponible(saldo);
        } else {
            throw new AppException(ErrorCode.TIPO_MOVIMIENTO_INVALIDO);
        }
        if(cuenta.getHistorico()==null){
            cuenta.setHistorico(List.of(movimiento));
        }else {
            cuenta.getHistorico().add(movimiento);
        }
        cuenta = cuentaService.saveHelper(cuenta);
        return cuenta.getHistorico().getFirst().toResponse();
    }

    @Override
    public List<MovimientoResponse> buscarPorNroCuenta(String nroCuenta) {
        Cuenta cuenta = cuentaService.recuperaPorNroCuenta(nroCuenta);
        return cuenta.getHistorico().stream().map(Movimiento::toResponse).toList();
    }
}
