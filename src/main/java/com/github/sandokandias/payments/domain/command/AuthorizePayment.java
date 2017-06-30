package com.github.sandokandias.payments.domain.command;

import com.github.sandokandias.payments.domain.shared.Command;
import com.github.sandokandias.payments.domain.vo.AccountId;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import com.github.sandokandias.payments.domain.vo.PaymentMethod;
import com.github.sandokandias.payments.domain.vo.Transaction;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class AuthorizePayment implements Command {
    public final PaymentId paymentId;
    public final AccountId accountId;
    public final PaymentMethod paymentMethod;
    public final Transaction transaction;
    public final LocalDateTime createdAt;
}
