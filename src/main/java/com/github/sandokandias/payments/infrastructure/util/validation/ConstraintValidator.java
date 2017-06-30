package com.github.sandokandias.payments.infrastructure.util.validation;


import io.vavr.control.Try;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

public final class ConstraintValidator {

    private static final ValidatorFactory VALIDATOR_FACTORY;

    static {
        VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    }

    public static <T> Try<T> validate(T value) {
        return Try.of(() -> {
            Set<ConstraintViolation<T>> validate = VALIDATOR_FACTORY.getValidator().validate(value);
            if (!validate.isEmpty()) {
                throw new ConstraintViolationException(validate);
            }
            return value;
        });

    }

    private ConstraintValidator() {
    }
}
