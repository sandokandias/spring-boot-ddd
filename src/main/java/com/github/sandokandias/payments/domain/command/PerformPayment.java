package com.github.sandokandias.payments.domain.command;

import com.github.sandokandias.payments.domain.shared.Command;
import com.github.sandokandias.payments.domain.vo.AccountId;
import com.github.sandokandias.payments.domain.vo.PaymentIntent;
import com.github.sandokandias.payments.domain.vo.PaymentMethod;
import com.github.sandokandias.payments.domain.vo.Transaction;
import com.github.sandokandias.payments.infrastructure.util.validation.ValidEnum;
import lombok.AllArgsConstructor;

import javax.validation.Valid;

@AllArgsConstructor
public class PerformPayment implements Command {
    @Valid
    public final AccountId accountId;
    @ValidEnum(conformsTo = PaymentIntent.class)
    public final String intent;
    @ValidEnum(conformsTo = PaymentMethod.class)
    public final String paymentMethod;
    @Valid
    public final Transaction transaction;
}
