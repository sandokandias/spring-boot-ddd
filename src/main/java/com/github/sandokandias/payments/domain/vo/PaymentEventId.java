package com.github.sandokandias.payments.domain.vo;

import com.github.sandokandias.payments.domain.shared.RandomUUID;


public class PaymentEventId extends RandomUUID {

    @Override
    protected String getPrefix() {
        return "PEV-%s";
    }
}