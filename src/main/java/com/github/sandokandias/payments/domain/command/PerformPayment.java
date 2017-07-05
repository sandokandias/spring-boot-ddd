package com.github.sandokandias.payments.domain.command;

import com.github.sandokandias.payments.domain.vo.CustomerId;
import com.github.sandokandias.payments.domain.vo.PaymentIntent;
import com.github.sandokandias.payments.domain.vo.PaymentMethod;
import com.github.sandokandias.payments.domain.vo.Transaction;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class PerformPayment implements PaymentCommand {
    public final CustomerId customerId;
    public final PaymentIntent intent;
    public final PaymentMethod paymentMethod;
    public final Transaction transaction;
    public final LocalDateTime createdAt;
}
