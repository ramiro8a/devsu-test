package com.devsu.domain.repository;

import com.devsu.app.rest.response.ReporteCuentaResponse;
import com.devsu.domain.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNroCuenta(String nroCuenta);

    @Query("""
    select new com.devsu.app.rest.response.ReporteCuentaResponse(
        c.id,
        c.nroCuenta,
        c.tipoCuenta,
        coalesce(
            (
                select m.saldo
                from Movimiento m
                where m.id = (
                    select max(m2.id)
                    from Movimiento m2
                    where m2.cuenta = c
                      and m2.fechaMovimiento < :desde
                )
            ),
            c.saldoInicial
        ),
        coalesce(
            (
                select m.saldo
                from Movimiento m
                where m.id = (
                    select max(m2.id)
                    from Movimiento m2
                    where m2.cuenta = c
                      and m2.fechaMovimiento <= :hasta
                )
            ),
            c.saldoInicial
        ),
        c.estado
    )
    from Cuenta c
    where c.cliente.id = :clienteId
    order by c.id desc
""")
    List<ReporteCuentaResponse> obtenerCuentasPorClienteConSaldoRango(
            @Param("clienteId") Long clienteId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );
}
