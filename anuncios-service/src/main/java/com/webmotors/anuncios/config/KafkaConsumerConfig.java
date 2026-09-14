package com.webmotors.anuncios.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.BackOffExecution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class KafkaConsumerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Bean
    CommonErrorHandler kafkaErrorHandler(
        KafkaTemplate<String, String> kafkaTemplate,
        MeterRegistry meterRegistry
    ) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
            kafkaTemplate,
            (ConsumerRecord<?, ?> record, Exception exception) ->
                new org.apache.kafka.common.TopicPartition(record.topic() + ".DLT", record.partition())
        );
        return new DefaultErrorHandler((record, exception) -> {
            meterRegistry.counter("desativacao_dlt_recebidas_total").increment();
            LOGGER.warn("Mensagem enviada para DLT: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset(), exception);
            recoverer.accept(record, exception);
        }, retryBackOff());
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
        ConsumerFactory<String, String> consumerFactory,
        CommonErrorHandler kafkaErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(kafkaErrorHandler);
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> batchKafkaListenerContainerFactory(
        ConsumerFactory<String, String> consumerFactory,
        CommonErrorHandler kafkaErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(kafkaErrorHandler);
        factory.setConcurrency(4);
        return factory;
    }

    private BackOff retryBackOff() {
        return () -> new BackOffExecution() {
            private final long[] delays = {1000L, 5000L, 30000L};
            private int attempt;

            @Override
            public long nextBackOff() {
                return attempt < delays.length ? delays[attempt++] : STOP;
            }
        };
    }
}