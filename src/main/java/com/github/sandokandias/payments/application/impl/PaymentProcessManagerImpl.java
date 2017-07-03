package com.github.sandokandias.payments.application.impl;

import com.github.sandokandias.payments.application.PaymentProcessManager;
import com.github.sandokandias.payments.domain.command.AuthorizePayment;
import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.entity.Payment;
import com.github.sandokandias.payments.domain.event.PaymentAuthorized;
import com.github.sandokandias.payments.domain.event.PaymentRequested;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import com.github.sandokandias.payments.domain.vo.PaymentStatus;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
class PaymentProcessManagerImpl implements PaymentProcessManager {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentProcessManagerImpl.class);

    private final ApplicationContext applicationContext;

    PaymentProcessManagerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public CompletionStage<Either<CommandFailure, Tuple2<PaymentId, PaymentStatus>>> process(PerformPayment performPayment) {

        LOG.debug("Payment process {}", performPayment);

        Payment payment = applicationContext.getBean(Payment.class);
        CompletionStage<Either<CommandFailure, PaymentRequested>> performPaymentPromise = payment.handle(performPayment);
        return performPaymentPromise.thenCompose(paymentRequested -> paymentRequested.fold(
                rejectPayment -> CompletableFuture.completedFuture(Either.left(rejectPayment)),
                acceptPayment -> {
                    if (acceptPayment.intent.isAuthorize()) {
                        AuthorizePayment authorizePayment = new AuthorizePayment(
                                acceptPayment.paymentId,
                                acceptPayment.customerId,
                                acceptPayment.paymentMethod,
                                acceptPayment.transaction,
                                LocalDateTime.now()
                        );
                        CompletionStage<Either<CommandFailure, PaymentAuthorized>> authorizePaymentPromise = payment.handle(authorizePayment);
                        return authorizePaymentPromise.thenApply(paymentAuthorized -> paymentAuthorized.fold(
                                rejectAuthorization -> Either.left(rejectAuthorization),
                                acceptAuthorization -> Either.right(Tuple.of(acceptAuthorization.paymentId, PaymentStatus.AUTHORIZED))
                        ));

                    } else {
                        return CompletableFuture.completedFuture(Either.right(Tuple.of(acceptPayment.paymentId, PaymentStatus.CAPTURED)));
                    }
                }
                )
        );

    }
}
