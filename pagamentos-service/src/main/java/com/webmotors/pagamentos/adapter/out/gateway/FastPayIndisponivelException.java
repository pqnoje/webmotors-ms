package com.webmotors.pagamentos.adapter.out.gateway;

/** Falha transitoria do gateway externo webmotors-fastpay, candidata a retry. */
public class FastPayIndisponivelException extends RuntimeException {
    public FastPayIndisponivelException(String message) {
        super(message);
    }
}
