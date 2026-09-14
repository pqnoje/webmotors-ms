package com.webmotors.usuarios.config;

import com.webmotors.usuarios.application.usecase.UsuarioOutboxService;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Bean
    NewTopic desativacaoPorMarcaTopic() {
        return new NewTopic(UsuarioOutboxService.TOPIC, 4, (short) 1)
            .configs(Map.of("retention.ms", "604800000"));
    }
}