package com.github.sandokandias.payments.domain.shared;

import com.github.sandokandias.payments.infrastructure.util.i18n.I18nCode;

import java.util.Set;

public class CommandFailure {
    public final Set<I18nCode> codes;

    private CommandFailure(Set<I18nCode> codes) {
        this.codes = codes;
    }

    public static CommandFailure of(Set<I18nCode> codes) {
        return new CommandFailure(codes);
    }
}
