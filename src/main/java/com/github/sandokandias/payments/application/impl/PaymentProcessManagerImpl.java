package com.github.sandokandias.payments.application.impl;

import com.github.sandokandias.payments.application.PaymentProcessManager;
import com.github.sandokandias.payments.domain.command.AuthorizePayment;
import com.github.sandokandias.payments.domain.command.ConfirmPayment;
import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.entity.Payment;
import com.github.sandokandias.payments.domain.event.PaymentAuthorized;
import com.github.sandokandias.payments.domain.event.PaymentConfirmed;
import com.github.sandokandias.payments.domain.event.PaymentEvent;
import com.github.sandokandias.payments.domain.event.PaymentRequested;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import com.github.sandokandias.payments.domain.vo.PaymentStatus;
import com.github.sandokandias.payments.infrastructure.util.i18n.I18nCode;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service
class PaymentProcessManagerImpl implements PaymentProcessManager {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentProcessManagerImpl.class);

    private final ApplicationContext applicationContext;

    PaymentProcessManagerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    //@HystrixCommand(fallbackMethod = "fallback")
    @Override
    public CompletionStage<Either<CommandFailure, Tuple2<PaymentId, PaymentStatus>>> process(PerformPayment performPayment) {

        LOG.debug("Payment process {}", performPayment);

        Payment payment = new Payment(applicationContext);
        CompletionStage<Either<CommandFailure, PaymentRequested>> performPaymentPromise = payment.handle(performPayment);
        return performPaymentPromise.thenCompose(paymentRequested -> paymentRequested.fold(
                rejectPayment -> completed(rejectPayment),
                acceptPayment -> authorize(payment, acceptPayment).thenCompose(paymentAuthorized -> paymentAuthorized.fold(
                        rejectAuthorization -> completed(rejectAuthorization),
                        acceptAuthorization -> {
                            if (acceptPayment.getPaymentIntent().isAuthorize()) {
                                return completed(acceptAuthorization, PaymentStatus.AUTHORIZED);
                            } else {
                                return confirm(payment, acceptPayment, acceptAuthorization);
                            }
                        }
                ))
        ));
    }

    public CompletionStage<Either<CommandFailure, Tuple2<PaymentId, PaymentStatus>>> fallback(PerformPayment performPayment) {
        return completed(new CommandFailure(new HashSet<I18nCode>() {{
            add(new I18nCode("SERVICE_UNAVAILABLE"));
        }}));
    }

    private CompletableFuture<Either<CommandFailure, Tuple2<PaymentId, PaymentStatus>>> completed(CommandFailure rejectAuthorization) {
        return completedFuture(left(rejectAuthorization));
    }

    private CompletableFuture<Either<CommandFailure, Tuple2<PaymentId, PaymentStatus>>> completed(PaymentEvent paymentEvent, PaymentStatus paymentStatus) {
        return completedFuture(right(Tuple.of(paymentEvent.getPaymentId(), paymentStatus)));
    }

    private CompletionStage<Either<CommandFailure, Tuple2<PaymentId, PaymentStatus>>> confirm(Payment payment, PaymentRequested acceptPayment, PaymentAuthorized acceptAuthorization) {
        ConfirmPayment confirmPayment = ConfirmPayment.commandOf(
                acceptAuthorization.getPaymentId(),
                acceptAuthorization.getCustomerId()
        );
        CompletionStage<Either<CommandFailure, PaymentConfirmed>> confirmPaymentPromise = payment.handle(confirmPayment);
        return confirmPaymentPromise.thenApply(paymentConfirmed -> paymentConfirmed.fold(
                rejectConfirmation -> left(rejectConfirmation),
                acceptConfirmation -> right(Tuple.of(acceptPayment.getPaymentId(), PaymentStatus.CAPTURED))
        ));
    }

    private CompletionStage<Either<CommandFailure, PaymentAuthorized>> authorize(Payment payment, PaymentRequested acceptPayment) {
        AuthorizePayment authorizePayment = AuthorizePayment.commandOf(
                acceptPayment.getPaymentId(),
                acceptPayment.getCustomerId(),
                acceptPayment.getPaymentMethod(),
                acceptPayment.getTransaction()
        );
        return payment.handle(authorizePayment);
    }
}
