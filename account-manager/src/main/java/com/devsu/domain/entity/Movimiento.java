package com.devsu.domain.entity;

import com.devsu.app.rest.response.MovimientoResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="t_movimiento")
public class Movimiento {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;
    @Column(name = "fecha_mov")
    private LocalDateTime fechaMovimiento;
    @Column(name = "tipo_mov")
    private Integer tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;

    public MovimientoResponse toResponse(){
        return new MovimientoResponse(
                this.getId(),
                this.getFechaMovimiento(),
                this.getTipoMovimiento(),
                TipoMovimiento.getDescription(this.getTipoMovimiento()),
                this.getValor(),
                this.getSaldo()
        );
    }
}
