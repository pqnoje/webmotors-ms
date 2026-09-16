package com.webmotors.pagamentos.adapter.in.http;

import com.webmotors.pagamentos.adapter.in.http.dto.CreatePaymentRequest;
import com.webmotors.pagamentos.adapter.in.http.dto.PaymentResponse;
import com.webmotors.pagamentos.application.port.in.PaymentUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PaymentController {
    private final PaymentUseCase useCase;

    public PaymentController(PaymentUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest request) {
        var payment = useCase.create(request.usuarioId(), request.amount(), request.method(), request.idempotencyKey());
        var response = PaymentResponse.from(payment);
        var status = payment.status() == com.webmotors.pagamentos.domain.model.PaymentStatus.COMPLETED
                ? HttpStatus.CREATED
                : HttpStatus.ACCEPTED;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping
    public List<PaymentResponse> findAll() {
        return useCase.findAll().stream().map(PaymentResponse::from).toList();
    }
}