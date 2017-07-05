package com.github.sandokandias.payments.domain.command;

import com.github.sandokandias.payments.domain.vo.CustomerId;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ConfirmPayment implements PaymentCommand {
    public final PaymentId paymentId;
    public final CustomerId customerId;
    public final LocalDateTime createdAt;
}
