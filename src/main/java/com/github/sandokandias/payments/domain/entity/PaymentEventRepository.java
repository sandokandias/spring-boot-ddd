package com.github.sandokandias.payments.domain.entity;


import com.github.sandokandias.payments.domain.event.PaymentAuthorized;
import com.github.sandokandias.payments.domain.event.PaymentConfirmed;
import com.github.sandokandias.payments.domain.event.PaymentRequested;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;

import java.util.concurrent.CompletionStage;

public interface PaymentEventRepository {
    CompletionStage<PaymentEventId> store(PaymentRequested paymentRequested);

    CompletionStage<PaymentEventId> store(PaymentAuthorized paymentAuthorized);

    CompletionStage<PaymentEventId> store(PaymentConfirmed paymentConfirmed);
}
