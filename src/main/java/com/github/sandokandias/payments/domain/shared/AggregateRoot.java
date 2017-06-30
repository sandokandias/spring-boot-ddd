package com.github.sandokandias.payments.domain.shared;

import io.vavr.control.Either;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public abstract class AggregateRoot<E, ID> implements Entity<E> {

    public final ID entityId;

    Map<Class<? extends Command>, CommandHandler<? extends Command, ? extends Event, ID>> handlers = new HashMap<>();

    protected AggregateRoot(ID entityId) {
        this.entityId = entityId;
    }

    protected <A extends Command, B extends Event> void setCommandHandler(Class<A> commandClass, CommandHandler<A, B, ID> handler) {
        handlers.put(commandClass, handler);
    }

    public <A extends Command, B extends Event> CompletionStage<Either<CommandFailure, B>> handle(A command) {
        CommandHandler<A, B, ID> commandHandler = (CommandHandler<A, B, ID>) handlers.get(command.getClass());
        return commandHandler.handle(command, entityId);
    }
}
