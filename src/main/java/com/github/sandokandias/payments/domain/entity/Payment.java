package com.github.sandokandias.payments.domain.entity;

import com.github.sandokandias.payments.domain.command.AuthorizePayment;
import com.github.sandokandias.payments.domain.command.ConfirmPayment;
import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.entity.handler.AuthorizePaymentHandler;
import com.github.sandokandias.payments.domain.entity.handler.ConfirmPaymentHandler;
import com.github.sandokandias.payments.domain.entity.handler.PerformPaymentHandler;
import com.github.sandokandias.payments.domain.shared.AggregateRoot;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import org.springframework.context.ApplicationContext;

public class Payment extends AggregateRoot<Payment, PaymentId> {

    public Payment(ApplicationContext applicationContext) {
        super(applicationContext, new PaymentId());
    }

    public Payment(ApplicationContext applicationContext, PaymentId paymentId) {
        super(applicationContext, paymentId);
    }

    @Override
    public boolean sameIdentityAs(Payment other) {
        return other != null && entityId.sameValueAs(other.entityId);
    }

    @Override
    protected AggregateRootBehavior initialBehavior() {
        AggregateRootBehaviorBuilder behaviorBuilder = new AggregateRootBehaviorBuilder();
        behaviorBuilder.setCommandHandler(PerformPayment.class, getHandler(PerformPaymentHandler.class));
        behaviorBuilder.setCommandHandler(AuthorizePayment.class, getHandler(AuthorizePaymentHandler.class));
        behaviorBuilder.setCommandHandler(ConfirmPayment.class, getHandler(ConfirmPaymentHandler.class));
        return behaviorBuilder.build();
    }
}
