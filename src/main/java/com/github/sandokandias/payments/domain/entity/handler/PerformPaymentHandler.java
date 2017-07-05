package com.github.sandokandias.payments.domain.entity.handler;

import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.command.validation.PerformPaymentValidator;
import com.github.sandokandias.payments.domain.entity.PaymentEventRepository;
import com.github.sandokandias.payments.domain.event.PaymentRequested;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.shared.CommandHandler;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component
public class PerformPaymentHandler implements
        CommandHandler<PerformPayment, PaymentRequested, PaymentId> {

    private static final Logger LOG = LoggerFactory.getLogger(PerformPaymentHandler.class);

    private final PaymentEventRepository paymentEventRepository;
    private final PerformPaymentValidator performPaymentValidator;

    PerformPaymentHandler(PaymentEventRepository paymentEventRepository,
                          PerformPaymentValidator performPaymentValidator) {
        this.paymentEventRepository = paymentEventRepository;
        this.performPaymentValidator = performPaymentValidator;
    }

    @Override
    public CompletionStage<Either<CommandFailure, PaymentRequested>> handle(PerformPayment command, PaymentId entityId) {

        LOG.debug("Handle command {}", command);

        return performPaymentValidator.acceptOrReject(command).fold(
                reject -> CompletableFuture.completedFuture(Either.left(reject)),
                accept -> {
                    PaymentRequested event = new PaymentRequested(
                            new PaymentEventId(),
                            entityId,
                            command.customerId,
                            command.intent,
                            command.paymentMethod,
                            command.transaction,
                            command.createdAt
                    );
                    CompletionStage<PaymentEventId> storePromise = paymentEventRepository.store(event);
                    return storePromise.thenApply(paymentEventId -> Either.right(event));
                }
        );
    }


}