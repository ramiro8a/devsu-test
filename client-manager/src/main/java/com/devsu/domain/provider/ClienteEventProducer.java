package com.devsu.domain.provider;

import com.devsu.domain.provider.dto.ClienteSincronizadoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClienteEventProducer {
    private static final String TOPIC_CLIENTE_CREADO = "cliente.creado";
    private static final String TOPIC_CLIENTE_ACTUALIZADO = "cliente.actualizado";
    private static final String TOPIC_CLIENTE_ELIMINADO = "cliente.eliminado";

    private final KafkaTemplate<String, ClienteSincronizadoEvent> kafkaTemplate;

    public void publicarClienteCreado(ClienteSincronizadoEvent event) {
        kafkaTemplate.send(TOPIC_CLIENTE_CREADO, event.clienteId().toString(), event);
    }

    public void publicarClienteActualizado(ClienteSincronizadoEvent event) {
        kafkaTemplate.send(TOPIC_CLIENTE_ACTUALIZADO, event.clienteId().toString(), event);
    }

    public void publicarClienteEliminado(ClienteSincronizadoEvent event) {
        kafkaTemplate.send(TOPIC_CLIENTE_ELIMINADO, event.clienteId().toString(), event);
    }
}
