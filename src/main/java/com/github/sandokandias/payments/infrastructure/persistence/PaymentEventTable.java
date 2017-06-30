package com.github.sandokandias.payments.infrastructure.persistence;


import com.github.sandokandias.payments.domain.vo.PaymentEventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class PaymentEventTable {
    @Id
    private String id;
    private String paymentId;
    private PaymentEventType eventType;
    private LocalDateTime createdAt;
    @Column(length = 1024)
    private String eventData;
}
