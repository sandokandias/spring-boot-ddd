package com.github.sandokandias.payments.domain.event;

import com.github.sandokandias.payments.domain.vo.CustomerId;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class PaymentConfirmed implements PaymentEvent {
    public final PaymentEventId paymentEventId;
    public final PaymentId paymentId;
    public final CustomerId customerId;
    public final LocalDateTime createdAt;
}
