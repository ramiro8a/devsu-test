package com.devsu.domain.repository;

import com.devsu.app.rest.response.ReporteMovimientoResponse;
import com.devsu.domain.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    @Query("""
        select new com.devsu.app.rest.response.ReporteMovimientoResponse(
            m.fechaMovimiento,
            c.nroCuenta,
            c.tipoCuenta,
            c.saldoInicial,
            c.estado,
            m.tipoMovimiento,
            m.valor,
            m.saldo
        )
        from Movimiento m
        join m.cuenta c
        join c.cliente cl
        where cl.id = :clienteId
          and m.fechaMovimiento between :desde and :hasta
        order by m.fechaMovimiento desc, m.id desc
    """)
    List<ReporteMovimientoResponse> obtenerMovimientosPorClienteYFechas(
            @Param("clienteId") Long clienteId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );
}
