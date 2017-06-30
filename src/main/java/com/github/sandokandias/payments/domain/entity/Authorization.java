package com.github.sandokandias.payments.domain.entity;

import com.github.sandokandias.payments.domain.shared.Entity;

public class Authorization implements Entity<Authorization> {


    @Override
    public boolean sameIdentityAs(Authorization other) {
        return false;
    }


}
