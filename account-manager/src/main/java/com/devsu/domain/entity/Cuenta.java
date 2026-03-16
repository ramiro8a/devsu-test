package com.devsu.domain.entity;

import com.devsu.app.rest.response.CuentaResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="t_cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
/*    @Column(name = "client_id")
    private Long clientId;*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClienteRef cliente;
    @Column(name = "nro_cuenta")
    private String nroCuenta;
    @Column(name = "tipo_cuenta")
    private Integer tipoCuenta;
    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;
    @Column(name = "saldo_disponible")
    private BigDecimal saldoDisponible;
    private Integer estado;
    @OneToMany(mappedBy = "cuenta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("id DESC")
    private List<Movimiento> historico = new ArrayList<>();

    public CuentaResponse toResponse() {
        return new CuentaResponse(
                this.getId(),
                this.getNroCuenta(),
                this.getTipoCuenta(),
                TipoCuenta.getDescription(this.getTipoCuenta()),
                this.getSaldoInicial(),
                this.getSaldoDisponible(),
                this.getEstado(),
                CuentaEstado.getDescription(this.getEstado()),
                this.getHistorico()==null ? new ArrayList<>() : this.getHistorico().stream().map(Movimiento::toResponse).toList()
        );
    }
}
