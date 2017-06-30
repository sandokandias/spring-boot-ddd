package com.github.sandokandias.payments.infrastructure.persistence;

import com.github.sandokandias.payments.domain.entity.PaymentEvent;
import org.springframework.data.repository.CrudRepository;

public interface EventStore extends CrudRepository<PaymentEvent, String> {
}
