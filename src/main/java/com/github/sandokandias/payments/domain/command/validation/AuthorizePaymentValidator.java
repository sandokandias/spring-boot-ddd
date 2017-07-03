package com.github.sandokandias.payments.domain.command.validation;

import com.github.sandokandias.payments.domain.command.AuthorizePayment;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.shared.CommandValidation;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

@Component
public class AuthorizePaymentValidator implements CommandValidation<AuthorizePayment> {

    @Override
    public Either<CommandFailure, AuthorizePayment> acceptOrReject(AuthorizePayment command) {
        return Either.right(command);
    }
}
