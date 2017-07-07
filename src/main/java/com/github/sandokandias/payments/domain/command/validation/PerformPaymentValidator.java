package com.github.sandokandias.payments.domain.command.validation;

import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.shared.CommandValidation;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

@Component
public class PerformPaymentValidator implements CommandValidation<PerformPayment> {

    @Override
    public Either<CommandFailure, PerformPayment> acceptOrReject(PerformPayment command) {

        return Either.right(command);
    }
}
