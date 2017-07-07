package com.github.sandokandias.payments.domain.vo;

import com.github.sandokandias.payments.domain.shared.RandomUUID;

public class PaymentId extends RandomUUID {

    public PaymentId() {
        super();
    }

    public PaymentId(String id) {
        super(id);
    }

    @Override
    protected String getPrefix() {
        return "PAY-%s";
    }
}
