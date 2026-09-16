package com.webmotors.pagamentos.application.port.out;

import com.webmotors.pagamentos.domain.model.Payment;

/**
 * Porta para o gateway externo webmotors-fastpay, responsavel por processar o pagamento (parcela do financiamento) via PIX/BOLETO/TED.
 */
public interface FastPayGatewayPort {
    void processarPagamento(Payment payment);
}
