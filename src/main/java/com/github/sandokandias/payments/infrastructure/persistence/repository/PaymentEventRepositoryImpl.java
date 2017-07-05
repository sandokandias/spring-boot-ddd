package com.github.sandokandias.payments.infrastructure.persistence.repository;


import com.github.sandokandias.payments.domain.entity.PaymentEventRepository;
import com.github.sandokandias.payments.domain.event.PaymentAuthorized;
import com.github.sandokandias.payments.domain.event.PaymentConfirmed;
import com.github.sandokandias.payments.domain.event.PaymentRequested;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;
import com.github.sandokandias.payments.domain.vo.PaymentEventType;
import com.github.sandokandias.payments.infrastructure.persistence.table.PaymentEventTable;
import com.github.sandokandias.payments.infrastructure.util.serialization.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Repository
class PaymentEventRepositoryImpl implements PaymentEventRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentEventRepositoryImpl.class);

    private final EventStore eventStore;
    private final JsonMapper jsonMapper;
    //private final ThreadPoolTaskExecutor executorContext;

    PaymentEventRepositoryImpl(EventStore eventStore,
                               JsonMapper jsonMapper
                               /*ThreadPoolTaskExecutor executorContext*/) {
        this.eventStore = eventStore;
        this.jsonMapper = jsonMapper;
        //this.executorContext = executorContext;
    }

    @Override
    public CompletionStage<PaymentEventId> store(PaymentRequested paymentRequested) {
        LOG.debug("Store paymentRequested {}", paymentRequested);
        String eventDataAsJson = jsonMapper.write(paymentRequested);
        PaymentEventTable paymentEventTable = new PaymentEventTable();
        paymentEventTable.setId(paymentRequested.paymentEventId.id);
        paymentEventTable.setEventType(PaymentEventType.PAYMENT_REQUESTED);
        paymentEventTable.setPaymentId(paymentRequested.paymentId.id);
        paymentEventTable.setCreatedAt(paymentRequested.createdAt);
        paymentEventTable.setEventData(eventDataAsJson);
        eventStore.save(paymentEventTable);
        return CompletableFuture.completedFuture(paymentRequested.paymentEventId);
    }

    @Override
    public CompletionStage<PaymentEventId> store(PaymentAuthorized paymentAuthorized) {
        LOG.debug("Store paymentAuthorized {}", paymentAuthorized);
        String eventDataAsJson = jsonMapper.write(paymentAuthorized);
        PaymentEventTable paymentEventTable = new PaymentEventTable();
        paymentEventTable.setId(paymentAuthorized.paymentEventId.id);
        paymentEventTable.setEventType(PaymentEventType.PAYMENT_AUTHORIZED);
        paymentEventTable.setPaymentId(paymentAuthorized.paymentId.id);
        paymentEventTable.setCreatedAt(paymentAuthorized.createdAt);
        paymentEventTable.setEventData(eventDataAsJson);
        eventStore.save(paymentEventTable);
        return CompletableFuture.completedFuture(paymentAuthorized.paymentEventId);
    }

    @Override
    public CompletionStage<PaymentEventId> store(PaymentConfirmed paymentConfirmed) {
        LOG.debug("Store paymentConfirmed {}", paymentConfirmed);
        String eventDataAsJson = jsonMapper.write(paymentConfirmed);
        PaymentEventTable paymentEventTable = new PaymentEventTable();
        paymentEventTable.setId(paymentConfirmed.paymentEventId.id);
        paymentEventTable.setEventType(PaymentEventType.PAYMENT_CONFIRMED);
        paymentEventTable.setPaymentId(paymentConfirmed.paymentId.id);
        paymentEventTable.setCreatedAt(paymentConfirmed.createdAt);
        paymentEventTable.setEventData(eventDataAsJson);
        eventStore.save(paymentEventTable);
        return CompletableFuture.completedFuture(paymentConfirmed.paymentEventId);
    }
}
