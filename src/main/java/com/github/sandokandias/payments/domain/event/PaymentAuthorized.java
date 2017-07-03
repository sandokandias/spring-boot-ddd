package com.github.sandokandias.payments.domain.event;

import com.github.sandokandias.payments.domain.vo.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class PaymentAuthorized implements PaymentEvent {
    public final PaymentEventId paymentEventId;
    public final PaymentId paymentId;
    public final CustomerId customerId;
    public final PaymentMethod paymentMethod;
    public final Transaction transaction;
    public final LocalDateTime createdAt;
}
