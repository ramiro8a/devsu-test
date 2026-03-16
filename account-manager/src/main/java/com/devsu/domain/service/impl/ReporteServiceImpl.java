package com.devsu.domain.service.impl;

import com.devsu.app.rest.request.EstadoCuentaFilter;
import com.devsu.app.rest.response.ReporteCuentaResponse;
import com.devsu.app.rest.response.ReporteEstadoCuentaResponse;
import com.devsu.app.rest.response.ReporteMovimientoResponse;
import com.devsu.commons.enums.ErrorCode;
import com.devsu.commons.exceptions.AppException;
import com.devsu.domain.entity.ClienteRef;
import com.devsu.domain.repository.ClienteRefRepository;
import com.devsu.domain.repository.CuentaRepository;
import com.devsu.domain.repository.MovimientoRepository;
import com.devsu.domain.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReporteServiceImpl implements ReporteService {
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final ClienteRefRepository clienteRefRepository;

    @Override
    public ReporteEstadoCuentaResponse obtenerReporte(EstadoCuentaFilter filter) {
        Optional<ClienteRef> clienteRef =clienteRefRepository.findById(filter.clientId());
        if (clienteRef.isEmpty()){
            throw new AppException(ErrorCode.NO_EXISTE_CLIENTE);
        }
        if (filter.desde().isAfter(filter.hasta())) {
            throw new AppException(ErrorCode.FECHA_DESDE_NO_PUEDE_SER_MAYOR_QUE_FECHA_HASTA);
        }
        LocalDateTime fechaDesde = filter.desde().atStartOfDay();
        LocalDateTime fechaHasta = filter.hasta().atTime(LocalTime.MAX);
        List<ReporteCuentaResponse> cuentas = cuentaRepository.obtenerCuentasPorClienteConSaldoRango(
                filter.clientId(), fechaDesde, fechaHasta
        );
        List<ReporteMovimientoResponse> movimientos =
                movimientoRepository.obtenerMovimientosPorClienteYFechas(
                        filter.clientId(),
                        fechaDesde,
                        fechaHasta
                );

        return new ReporteEstadoCuentaResponse(
                filter.clientId(),
                clienteRef.get().getIdentificacion(),
                clienteRef.get().getNombres(),
                filter.desde(),
                filter.hasta(),
                cuentas,
                movimientos
        );
    }
}
