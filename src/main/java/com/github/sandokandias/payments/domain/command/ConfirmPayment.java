package com.github.sandokandias.payments.domain.command;

import com.github.sandokandias.payments.domain.vo.CustomerId;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import lombok.Value;

import java.time.LocalDateTime;

@Value(staticConstructor = "commandOf")
public class ConfirmPayment implements PaymentCommand {
    private final PaymentId paymentId;
    private final CustomerId customerId;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
