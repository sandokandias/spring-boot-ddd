package com.github.sandokandias.payments.domain.event;

import com.github.sandokandias.payments.domain.shared.Event;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;
import com.github.sandokandias.payments.domain.vo.PaymentEventType;
import com.github.sandokandias.payments.domain.vo.PaymentId;

import java.time.LocalDateTime;

public interface PaymentEvent extends Event {
    PaymentEventId getEventId();

    PaymentEventType getEventType();

    PaymentId getPaymentId();

    LocalDateTime getTimestamp();
}
