package com.devsu.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="t_client_referencia")
public class ClienteRef {
    @Id
    @Column(name = "client_id")
    private Long id;
    private String identificacion;
    private String nombres;
    private Integer estado;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Cuenta> cuentas;
}
