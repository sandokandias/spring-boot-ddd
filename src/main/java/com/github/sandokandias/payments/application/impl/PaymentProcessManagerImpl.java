package com.github.sandokandias.payments.application.impl;

import com.github.sandokandias.payments.application.PaymentProcessManager;
import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.entity.Payment;
import com.github.sandokandias.payments.domain.event.PaymentRequested;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import io.vavr.control.Either;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionStage;

@Service
class PaymentProcessManagerImpl implements PaymentProcessManager {

    private final ApplicationContext applicationContext;

    PaymentProcessManagerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public CompletionStage<Either<CommandFailure, PaymentId>> process(PerformPayment performPayment) {
        Payment payment = applicationContext.getBean(Payment.class);
        CompletionStage<Either<CommandFailure, PaymentRequested>> promise = payment.handle(performPayment);
        return promise.thenApply(result -> result.fold(
                reject -> Either.left(reject),
                accept -> Either.right(accept.paymentId)
                )
        );

    }
}
