package com.github.sandokandias.payments.application.impl;

import com.github.sandokandias.payments.application.PaymentProcessManager;
import com.github.sandokandias.payments.domain.command.AuthorizePayment;
import com.github.sandokandias.payments.domain.command.ConfirmPayment;
import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.entity.Payment;
import com.github.sandokandias.payments.domain.event.PaymentAuthorized;
import com.github.sandokandias.payments.domain.event.PaymentConfirmed;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@Transactional(propagation = Propagation.REQUIRES_NEW)
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

        Payment payment = new Payment(applicationContext);
        CompletionStage<Either<CommandFailure, PaymentRequested>> performPaymentPromise = payment.handle(performPayment);
        return performPaymentPromise.thenCompose(paymentRequested -> paymentRequested.fold(
                rejectPayment -> CompletableFuture.completedFuture(left(rejectPayment)),
                acceptPayment -> {
                    AuthorizePayment authorizePayment = createAuthorizePayment(acceptPayment);
                    CompletionStage<Either<CommandFailure, PaymentAuthorized>> authorizePaymentPromise = payment.handle(authorizePayment);
                    return authorizePaymentPromise.thenCompose(paymentAuthorized -> paymentAuthorized.fold(
                            rejectAuthorization -> CompletableFuture.completedFuture(left(rejectAuthorization)),
                            acceptAuthorization -> {
                                if (acceptPayment.intent.isAuthorize()) {
                                    return CompletableFuture.completedFuture(right(Tuple.of(acceptAuthorization.paymentId, PaymentStatus.AUTHORIZED)));
                                } else {
                                    ConfirmPayment confirmPayment = createConfirmPayment(acceptAuthorization);
                                    CompletionStage<Either<CommandFailure, PaymentConfirmed>> confirmPaymentPromise = payment.handle(confirmPayment);
                                    return confirmPaymentPromise.thenApply(paymentConfirmed -> paymentConfirmed.fold(
                                            rejectConfirmation -> left(rejectConfirmation),
                                            acceptConfirmation -> right(Tuple.of(acceptPayment.paymentId, PaymentStatus.CAPTURED))
                                    ));
                                }

                            }
                    ));
                }
        ));

    }

    private ConfirmPayment createConfirmPayment(PaymentAuthorized acceptAuthorization) {
        return new ConfirmPayment(
                acceptAuthorization.paymentId,
                acceptAuthorization.customerId,
                LocalDateTime.now()
        );
    }

    private AuthorizePayment createAuthorizePayment(PaymentRequested acceptPayment) {
        return new AuthorizePayment(
                acceptPayment.paymentId,
                acceptPayment.customerId,
                acceptPayment.paymentMethod,
                acceptPayment.transaction,
                LocalDateTime.now()
        );
    }
}
