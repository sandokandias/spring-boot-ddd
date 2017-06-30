package com.github.sandokandias.payments.infrastructure.persistence.repository;

import com.github.sandokandias.payments.infrastructure.persistence.table.PaymentEventTable;
import org.springframework.data.repository.CrudRepository;

public interface EventStore extends CrudRepository<PaymentEventTable, String> {
}
