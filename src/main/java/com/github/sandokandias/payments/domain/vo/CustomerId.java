package com.github.sandokandias.payments.domain.vo;

import com.github.sandokandias.payments.domain.shared.RandomUUID;

public class CustomerId extends RandomUUID {

    public CustomerId(String id) {
        super(id);
    }

    @Override
    protected String getPrefix() {
        return "CST-%s";
    }
}
