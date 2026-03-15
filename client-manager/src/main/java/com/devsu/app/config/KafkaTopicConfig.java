package com.devsu.app.config;

import com.devsu.domain.provider.dto.ClienteSincronizadoEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic clienteCreadoTopic() {
        return TopicBuilder.name("cliente.creado").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic clienteActualizadoTopic() {
        return TopicBuilder.name("cliente.actualizado").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic clienteEliminadoTopic() {
        return TopicBuilder.name("cliente.eliminado").partitions(1).replicas(1).build();
    }

    @Bean
    public ProducerFactory<String, ClienteSincronizadoEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, ClienteSincronizadoEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
