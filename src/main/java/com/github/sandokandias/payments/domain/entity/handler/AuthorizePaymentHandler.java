package com.github.sandokandias.payments.domain.entity.handler;

import com.github.sandokandias.payments.domain.command.AuthorizePayment;
import com.github.sandokandias.payments.domain.entity.PaymentEventRepository;
import com.github.sandokandias.payments.domain.event.PaymentAuthorized;
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
public class AuthorizePaymentHandler implements CommandHandler<AuthorizePayment, PaymentAuthorized, PaymentId> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizePaymentHandler.class);

    private final PaymentEventRepository paymentEventRepository;

    public AuthorizePaymentHandler(PaymentEventRepository paymentEventRepository) {
        this.paymentEventRepository = paymentEventRepository;
    }

    @Override
    public CompletionStage<Either<CommandFailure, PaymentAuthorized>> handle(AuthorizePayment command, PaymentId entityId) {

        LOG.debug("Handle command {}", command);

        return command.acceptOrReject().fold(
                reject -> CompletableFuture.completedFuture(Either.left(reject)),
                accept -> {
                    PaymentAuthorized event = new PaymentAuthorized(
                            new PaymentEventId(),
                            entityId,
                            command.customerId,
                            command.paymentMethod,
                            command.transaction,
                            LocalDateTime.now()
                    );
                    CompletionStage<PaymentEventId> storePromise = paymentEventRepository.store(event);
                    return storePromise.thenApply(paymentEventId -> Either.right(event));
                }
        );
    }
}
