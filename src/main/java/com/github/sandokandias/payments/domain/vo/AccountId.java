package com.github.sandokandias.payments.domain.vo;

import com.github.sandokandias.payments.domain.shared.RandomUUID;

public class AccountId extends RandomUUID {

    public AccountId(String id) {
        super(id);
    }

    @Override
    protected String getPrefix() {
        return "ACC-%s";
    }
}
