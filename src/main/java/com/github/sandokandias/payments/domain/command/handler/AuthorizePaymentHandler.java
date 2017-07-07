package com.github.sandokandias.payments.domain.command.handler;

import com.github.sandokandias.payments.domain.command.AuthorizePayment;
import com.github.sandokandias.payments.domain.command.validation.AuthorizePaymentValidator;
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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component
public class AuthorizePaymentHandler implements CommandHandler<AuthorizePayment, PaymentAuthorized, PaymentId> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizePaymentHandler.class);

    private final PaymentEventRepository paymentEventRepository;
    private final AuthorizePaymentValidator authorizePaymentValidator;


    public AuthorizePaymentHandler(PaymentEventRepository paymentEventRepository,
                                   AuthorizePaymentValidator authorizePaymentValidator) {
        this.paymentEventRepository = paymentEventRepository;
        this.authorizePaymentValidator = authorizePaymentValidator;
    }

    @Override
    public CompletionStage<Either<CommandFailure, PaymentAuthorized>> handle(AuthorizePayment command, PaymentId entityId) {

        LOG.debug("Handle command {}", command);

        return authorizePaymentValidator.acceptOrReject(command).fold(
                reject -> CompletableFuture.completedFuture(Either.left(reject)),
                accept -> {
                    PaymentAuthorized event = PaymentAuthorized.eventOf(
                            entityId,
                            command.getCustomerId(),
                            command.getPaymentMethod(),
                            command.getTransaction(),
                            command.getTimestamp()
                    );
                    CompletionStage<PaymentEventId> storePromise = paymentEventRepository.store(event);
                    return storePromise.thenApply(paymentEventId -> Either.right(event));
                }
        );
    }
}
