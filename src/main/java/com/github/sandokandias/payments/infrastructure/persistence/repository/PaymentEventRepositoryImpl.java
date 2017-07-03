package com.github.sandokandias.payments.infrastructure.persistence.repository;


import com.github.sandokandias.payments.domain.entity.PaymentEventRepository;
import com.github.sandokandias.payments.domain.event.PaymentAuthorized;
import com.github.sandokandias.payments.domain.event.PaymentRequested;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;
import com.github.sandokandias.payments.domain.vo.PaymentEventType;
import com.github.sandokandias.payments.infrastructure.persistence.table.PaymentEventTable;
import com.github.sandokandias.payments.infrastructure.util.serialization.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Repository
class PaymentEventRepositoryImpl implements PaymentEventRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentEventRepositoryImpl.class);

    private final EventStore eventStore;
    private final JsonMapper jsonMapper;
    private final Executor dbPool;

    PaymentEventRepositoryImpl(EventStore eventStore,
                               JsonMapper jsonMapper) {
        this.eventStore = eventStore;
        this.jsonMapper = jsonMapper;
        this.dbPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public CompletionStage<PaymentEventId> store(PaymentRequested paymentRequested) {
        LOG.debug("Store paymentRequested {}", paymentRequested);
        return CompletableFuture.supplyAsync(() -> {
            LOG.debug("CompletableFuture supplyAsync store paymentRequested {}", paymentRequested);
            String eventDataAsJson = jsonMapper.write(paymentRequested);
            PaymentEventTable paymentEventTable = new PaymentEventTable();
            paymentEventTable.setId(paymentRequested.paymentEventId.id);
            paymentEventTable.setEventType(PaymentEventType.PAYMENT_REQUESTED);
            paymentEventTable.setPaymentId(paymentRequested.paymentId.id);
            paymentEventTable.setCreatedAt(LocalDateTime.now());
            paymentEventTable.setEventData(eventDataAsJson);
            eventStore.save(paymentEventTable);
            return paymentRequested.paymentEventId;
        }, dbPool);
    }

    @Override
    public CompletionStage<PaymentEventId> store(PaymentAuthorized paymentAuthorized) {
        LOG.debug("Store paymentAuthorized {}", paymentAuthorized);
        return CompletableFuture.supplyAsync(() -> {
            LOG.debug("CompletableFuture supplyAsync store paymentAuthorized {}", paymentAuthorized);
            String eventDataAsJson = jsonMapper.write(paymentAuthorized);
            PaymentEventTable paymentEventTable = new PaymentEventTable();
            paymentEventTable.setId(paymentAuthorized.paymentEventId.id);
            paymentEventTable.setEventType(PaymentEventType.PAYMENT_AUTHORIZED);
            paymentEventTable.setPaymentId(paymentAuthorized.paymentId.id);
            paymentEventTable.setCreatedAt(LocalDateTime.now());
            paymentEventTable.setEventData(eventDataAsJson);
            eventStore.save(paymentEventTable);
            return paymentAuthorized.paymentEventId;
        }, dbPool);
    }
}
