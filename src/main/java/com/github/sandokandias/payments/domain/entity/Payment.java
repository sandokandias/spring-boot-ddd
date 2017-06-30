package com.github.sandokandias.payments.domain.entity;

import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.event.PaymentRequested;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.shared.Entity;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import com.github.sandokandias.payments.domain.vo.PaymentIntent;
import com.github.sandokandias.payments.domain.vo.PaymentMethod;
import io.vavr.control.Either;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Payment implements Entity<Payment> {

    private final PaymentEventRepository paymentEventRepository;

    public final PaymentId id;

    public Payment(PaymentEventRepository paymentEventRepository) {
        this.paymentEventRepository = paymentEventRepository;
        this.id = new PaymentId();
    }

    @Override
    public boolean sameIdentityAs(Payment other) {
        return false;
    }

    public CompletionStage<Either<CommandFailure, PaymentRequested>> handle(PerformPayment command) {
        return command.acceptOrReject().fold(
                reject -> CompletableFuture.completedFuture(Either.left(reject)),
                accept -> {
                    PaymentRequested event = new PaymentRequested(
                            new PaymentEventId(),
                            this.id,
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
