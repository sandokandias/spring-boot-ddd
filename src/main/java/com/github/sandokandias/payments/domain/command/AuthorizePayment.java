package com.github.sandokandias.payments.domain.command;

import com.github.sandokandias.payments.domain.vo.CustomerId;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import com.github.sandokandias.payments.domain.vo.PaymentMethod;
import com.github.sandokandias.payments.domain.vo.Transaction;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class AuthorizePayment implements PaymentCommand {
    public final PaymentId paymentId;
    public final CustomerId customerId;
    public final PaymentMethod paymentMethod;
    public final Transaction transaction;
    public final LocalDateTime createdAt;
}
