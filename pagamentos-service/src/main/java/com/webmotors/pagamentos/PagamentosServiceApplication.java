package com.webmotors.pagamentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PagamentosServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PagamentosServiceApplication.class, args);
    }
}