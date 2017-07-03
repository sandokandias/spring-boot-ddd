package com.github.sandokandias.payments.infrastructure.util.i18n;

import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

public class I18nFactory {

    public static Set<I18nCode> i18nCodeFrom(ConstraintViolationException exception) {
        return exception
                .getConstraintViolations()
                .stream()
                .map(c -> new I18nCode(
                        c.getMessage(),
                        new Object[]{c.getPropertyPath().iterator().next().getName()}))
                .collect(Collectors.toSet());
    }
}
