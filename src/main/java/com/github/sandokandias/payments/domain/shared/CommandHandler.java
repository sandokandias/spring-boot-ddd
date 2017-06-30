package com.github.sandokandias.payments.domain.shared;

import io.vavr.control.Either;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface CommandHandler<C extends Command, E extends Event> {
    CompletionStage<Either<CommandFailure, E>> handle(C command);
}
