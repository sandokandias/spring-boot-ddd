package com.github.sandokandias.payments.domain.shared;

import com.github.sandokandias.payments.infrastructure.util.i18n.I18nCode;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class CommandFailure {
    public final Set<I18nCode> codes;
}
