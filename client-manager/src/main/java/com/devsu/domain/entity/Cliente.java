package com.devsu.domain.entity;

import com.devsu.app.rest.response.ClienteResponse;
import com.devsu.domain.provider.dto.ClienteSincronizadoEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="t_client")
public class Cliente extends Persona{
    private Integer estado;
    private String password;

    public ClienteResponse toResponse() {
        return new ClienteResponse(
                this.getId(),
                this.getNombres(),
                this.getGenero(),
                this.getEdad(),
                this.getIdentificacion(),
                this.getDireccion(),
                this.getTelefono(),
                this.getEstado()
        );
    }

    public ClienteSincronizadoEvent toSincEvent(String accion) {
        return new ClienteSincronizadoEvent(
                this.getId(),
                this.getIdentificacion(),
                this.getNombres(),
                this.getEstado(),
                accion
        );
    }
}
