package com.devsu.app.config;

import com.devsu.app.messaging.dto.ClienteSincronizadoEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, ClienteSincronizadoEvent> consumerFactory() {
        JsonDeserializer<ClienteSincronizadoEvent> deserializer =
                new JsonDeserializer<>(ClienteSincronizadoEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.ignoreTypeHeaders();

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "account-manager");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClienteSincronizadoEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ClienteSincronizadoEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

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
}
