package com.github.sandokandias.payments.application;


import com.github.sandokandias.payments.domain.command.PerformPayment;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import com.github.sandokandias.payments.domain.vo.PaymentStatus;
import io.vavr.Tuple2;
import io.vavr.control.Either;

import java.util.concurrent.CompletionStage;

public interface PaymentProcessManager {
    CompletionStage<Either<CommandFailure, Tuple2<PaymentId, PaymentStatus>>> process(PerformPayment command);
}
