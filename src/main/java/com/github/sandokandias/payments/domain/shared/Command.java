package com.github.sandokandias.payments.domain.shared;

import com.github.sandokandias.payments.infrastructure.util.i18n.I18nFactory;
import com.github.sandokandias.payments.infrastructure.util.validation.ConstraintValidator;
import io.vavr.control.Either;

import javax.validation.ConstraintViolationException;

public interface Command {

    default <C> Either<CommandFailure, C> acceptOrReject() {
        return ConstraintValidator.validate(this)
                .toEither()
                .bimap(l -> CommandFailure.of(I18nFactory.i18nCodeFrom((ConstraintViolationException) l)),
                        r -> (C) r);
    }
}
