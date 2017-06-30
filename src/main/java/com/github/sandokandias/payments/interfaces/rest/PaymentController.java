package com.github.sandokandias.payments.interfaces.rest;

import com.github.sandokandias.payments.application.PaymentProcessManager;
import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.vo.AccountId;
import com.github.sandokandias.payments.domain.vo.IdRepresentation;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import com.github.sandokandias.payments.interfaces.request.PerformPaymentRequest;
import io.vavr.control.Either;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletionStage;

@RestController
public class PaymentController {

    private final PaymentProcessManager paymentProcessManager;

    public PaymentController(PaymentProcessManager paymentProcessManager) {
        this.paymentProcessManager = paymentProcessManager;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<ResponseEntity<?>> create(@RequestBody PerformPaymentRequest request) {

        PerformPayment command = new PerformPayment(
                new AccountId(request.getAccountId()),
                request.getIntent(),
                request.getPaymentMethod(),
                request.getTransaction());

        CompletionStage<Either<CommandFailure, PaymentId>> promise = paymentProcessManager.process(command);
        return promise.thenApply(acceptOrReject -> acceptOrReject.fold(
                reject -> ResponseEntity.badRequest().body(reject),
                accept -> ResponseEntity.accepted().body(new IdRepresentation(accept.id))
        ));

    }
}
