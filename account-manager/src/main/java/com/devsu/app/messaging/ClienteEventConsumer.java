package com.devsu.app.messaging;

import com.devsu.app.messaging.dto.ClienteSincronizadoEvent;
import com.devsu.domain.service.ClienteRefService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteEventConsumer {
    private final ClienteRefService clienteRefService;

    @Transactional
    @KafkaListener(topics = "cliente.creado", groupId = "account-manager")
    public void escucharClienteCreado(ClienteSincronizadoEvent event) {
        clienteRefService.crea(event);
    }

    @Transactional
    @KafkaListener(topics = "cliente.actualizado", groupId = "account-manager")
    public void escucharClienteActualizado(ClienteSincronizadoEvent event) {
        clienteRefService.actualiza(event);
    }

    @Transactional
    @KafkaListener(topics = "cliente.eliminado", groupId = "account-manager")
    public void escucharClienteEliminado(ClienteSincronizadoEvent event) {
        clienteRefService.elimina(event);
    }
}
