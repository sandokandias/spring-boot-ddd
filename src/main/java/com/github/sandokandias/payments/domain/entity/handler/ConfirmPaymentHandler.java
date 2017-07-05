package com.github.sandokandias.payments.domain.entity.handler;

import com.github.sandokandias.payments.domain.command.ConfirmPayment;
import com.github.sandokandias.payments.domain.entity.PaymentEventRepository;
import com.github.sandokandias.payments.domain.event.PaymentConfirmed;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.shared.CommandHandler;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;

@Component
public class ConfirmPaymentHandler implements CommandHandler<ConfirmPayment, PaymentConfirmed, PaymentId> {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmPaymentHandler.class);

    private final PaymentEventRepository paymentEventRepository;


    public ConfirmPaymentHandler(PaymentEventRepository paymentEventRepository) {
        this.paymentEventRepository = paymentEventRepository;
    }

    @Override
    public CompletionStage<Either<CommandFailure, PaymentConfirmed>> handle(ConfirmPayment command, PaymentId entityId) {

        LOG.debug("Handle command {}", command);

        PaymentConfirmed event = new PaymentConfirmed(
                new PaymentEventId(),
                entityId,
                command.customerId,
                command.createdAt
        );
        CompletionStage<PaymentEventId> storePromise = paymentEventRepository.store(event);
        return storePromise.thenApply(paymentEventId -> Either.right(event));

    }
}
