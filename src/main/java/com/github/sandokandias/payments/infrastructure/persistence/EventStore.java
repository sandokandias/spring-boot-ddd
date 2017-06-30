package com.github.sandokandias.payments.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;

public interface EventStore extends CrudRepository<PaymentEventTable, String> {
}
