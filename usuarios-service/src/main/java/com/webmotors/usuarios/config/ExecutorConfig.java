package com.webmotors.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {
    @Bean(destroyMethod = "shutdown")
    ExecutorService desativacaoExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}