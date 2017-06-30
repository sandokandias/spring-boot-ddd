package com.github.sandokandias.payments.domain.event;

import com.github.sandokandias.payments.domain.shared.Event;
import com.github.sandokandias.payments.domain.vo.AccountId;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import com.github.sandokandias.payments.domain.vo.PaymentMethod;
import com.github.sandokandias.payments.domain.vo.Transaction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaymentAuthorized implements Event {
    public final PaymentId paymentId;
    public final AccountId accountId;
    public final PaymentMethod paymentMethod;
    public final Transaction transaction;
}
