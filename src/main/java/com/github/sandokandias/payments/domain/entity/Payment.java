package com.github.sandokandias.payments.domain.entity;

import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.entity.handler.PerformPaymentHandler;
import com.github.sandokandias.payments.domain.shared.AggregateRoot;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Payment extends AggregateRoot<Payment, PaymentId> {

    public Payment() {
        super(new PaymentId());
    }

    @Override
    public boolean sameIdentityAs(Payment other) {
        return false;
    }

    @Autowired
    public void setPerformPaymentHandler(PerformPaymentHandler performPaymentHandler) {
        setCommandHandler(PerformPayment.class, performPaymentHandler);
    }
}
