package com.github.sandokandias.payments.domain.entity;

import com.github.sandokandias.payments.domain.command.AuthorizePayment;
import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.entity.handler.AuthorizePaymentHandler;
import com.github.sandokandias.payments.domain.entity.handler.PerformPaymentHandler;
import com.github.sandokandias.payments.domain.shared.AggregateRoot;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Payment extends AggregateRoot<Payment, PaymentId> {

    public Payment(ApplicationContext applicationContext) {
        super(new PaymentId(), applicationContext);
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
        return behaviorBuilder.build();
    }
}
