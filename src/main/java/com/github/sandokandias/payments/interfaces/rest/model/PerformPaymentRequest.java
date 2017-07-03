package com.github.sandokandias.payments.interfaces.rest.model;

import com.github.sandokandias.payments.domain.vo.PaymentIntent;
import com.github.sandokandias.payments.domain.vo.PaymentMethod;
import com.github.sandokandias.payments.domain.vo.Transaction;
import com.github.sandokandias.payments.infrastructure.util.validation.ValidEnum;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class PerformPaymentRequest {
    @NotNull
    private String customerId;
    @ValidEnum(conformsTo = PaymentIntent.class)
    private String intent;
    @ValidEnum(conformsTo = PaymentMethod.class)
    private String paymentMethod;
    @Valid
    private Transaction transaction;
}
