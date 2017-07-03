package com.github.sandokandias.payments.domain.event;

import com.github.sandokandias.payments.domain.vo.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class PaymentRequested implements PaymentEvent {
    public final PaymentEventId paymentEventId;
    public final PaymentId paymentId;
    public final CustomerId customerId;
    public final PaymentIntent intent;
    public final PaymentMethod paymentMethod;
    public final Transaction transaction;
    public final LocalDateTime createdAt;
}
