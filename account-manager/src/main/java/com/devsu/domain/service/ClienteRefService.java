package com.devsu.domain.service;

import com.devsu.app.messaging.dto.ClienteSincronizadoEvent;
import com.devsu.domain.entity.ClienteRef;

public interface ClienteRefService {
    void crea(ClienteSincronizadoEvent data);
    void actualiza(ClienteSincronizadoEvent data);
    void elimina(ClienteSincronizadoEvent data);
    ClienteRef buscaPorId(Long id);
}
