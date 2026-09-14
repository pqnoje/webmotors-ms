package com.webmotors.anuncios.config;

import com.webmotors.anuncios.adapter.in.kafka.DesativacaoPorMarcaBatchConsumer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    public static final String DLT = DesativacaoPorMarcaBatchConsumer.TOPIC + ".DLT";

    @Bean
    NewTopic desativacaoPorMarcaTopic() {
        return new NewTopic(DesativacaoPorMarcaBatchConsumer.TOPIC, 4, (short) 1);
    }

    @Bean
    NewTopic desativacaoPorMarcaDlt() {
        return new NewTopic(DLT, 4, (short) 1)
            .configs(Map.of("retention.ms", "1209600000"));
    }
}