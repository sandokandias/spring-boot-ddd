package com.github.sandokandias.payments.domain.shared;


public interface EventPublisher<E extends Event> {

    void apply(E event);
}

