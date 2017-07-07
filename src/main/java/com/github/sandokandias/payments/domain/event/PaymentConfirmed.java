package com.github.sandokandias.payments.domain.event;

import com.github.sandokandias.payments.domain.vo.CustomerId;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;
import com.github.sandokandias.payments.domain.vo.PaymentEventType;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import lombok.Value;

import java.time.LocalDateTime;

@Value(staticConstructor = "eventOf")
public class PaymentConfirmed implements PaymentEvent {
    private final PaymentEventId eventId = new PaymentEventId();
    private final PaymentId paymentId;
    private final CustomerId customerId;
    private final LocalDateTime timestamp;

    @Override
    public PaymentEventType getEventType() {
        return PaymentEventType.PAYMENT_CONFIRMED;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
