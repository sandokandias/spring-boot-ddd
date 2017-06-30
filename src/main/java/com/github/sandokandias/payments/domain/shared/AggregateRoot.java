package com.github.sandokandias.payments.domain.shared;

import io.vavr.control.Either;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public abstract class AggregateRoot<E, ID> implements Entity<E> {

    public final ID entityId;
    private final ApplicationContext applicationContext;
    private AggregateRootBehavior behavior;

    protected AggregateRoot(ID entityId, ApplicationContext applicationContext) {
        this.entityId = entityId;
        this.applicationContext = applicationContext;
        this.behavior = initialBehavior();
    }

    public <A extends Command, B extends Event> CompletionStage<Either<CommandFailure, B>> handle(A command) {
        CommandHandler<A, B, ID> commandHandler = (CommandHandler<A, B, ID>) behavior.handlers.get(command.getClass());
        return commandHandler.handle(command, entityId);
    }

    protected <A extends Command, B extends Event> CommandHandler<A, B, ID> getHandler(Class<? extends CommandHandler> commandHandlerClass) {
        return applicationContext.getBean(commandHandlerClass);
    }

    protected abstract AggregateRootBehavior initialBehavior();


    public class AggregateRootBehavior<ID> {

        protected final Map<Class<? extends Command>, CommandHandler<? extends Command, ? extends Event, ID>> handlers;

        public AggregateRootBehavior(Map<Class<? extends Command>, CommandHandler<? extends Command, ? extends Event, ID>> handlers) {
            this.handlers = Collections.unmodifiableMap(handlers);
        }
    }

    public class AggregateRootBehaviorBuilder<ID> {

        private final Map<Class<? extends Command>, CommandHandler<? extends Command, ? extends Event, ID>> handlers = new HashMap<>();

        public <A extends Command, B extends Event> AggregateRootBehaviorBuilder setCommandHandler(Class<A> commandClass, CommandHandler<A, B, ID> handler) {
            handlers.put(commandClass, handler);
            return this;
        }

        public AggregateRootBehavior build() {
            return new AggregateRootBehavior(handlers);
        }
    }
}
