package com.github.sandokandias.payments.domain.entity.handler;

import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.entity.PaymentEventRepository;
import com.github.sandokandias.payments.domain.event.PaymentRequested;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.shared.CommandHandler;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import com.github.sandokandias.payments.domain.vo.PaymentIntent;
import com.github.sandokandias.payments.domain.vo.PaymentMethod;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component
public class PerformPaymentHandler implements CommandHandler<PerformPayment, PaymentRequested, PaymentId> {

    private final PaymentEventRepository paymentEventRepository;

    PerformPaymentHandler(PaymentEventRepository paymentEventRepository) {
        this.paymentEventRepository = paymentEventRepository;
    }

    @Override
    public CompletionStage<Either<CommandFailure, PaymentRequested>> handle(PerformPayment command, PaymentId entityId) {
        return command.acceptOrReject().fold(
                reject -> CompletableFuture.completedFuture(Either.left(reject)),
                accept -> {
                    PaymentRequested event = new PaymentRequested(
                            new PaymentEventId(),
                            entityId,
                            command.accountId,
                            PaymentIntent.valueOf(command.intent),
                            PaymentMethod.valueOf(command.paymentMethod),
                            command.transaction,
                            LocalDateTime.now()
                    );
                    CompletionStage<PaymentEventId> storePromise = paymentEventRepository.store(event);
                    return storePromise.thenApply(paymentEventId -> Either.right(event));
                }
        );
    }
}