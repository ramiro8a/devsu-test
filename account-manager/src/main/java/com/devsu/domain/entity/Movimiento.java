package com.devsu.domain.entity;

import com.devsu.app.rest.response.MovimientoResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @OneToOne(fetch = FetchType.LAZY)
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
