package com.github.sandokandias.payments.infrastructure.persistence.mapping;


import com.github.sandokandias.payments.domain.vo.PaymentEventType;
import lombok.*;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"eventData", "timestamp"})
@ToString
@Entity
@Table
public class PaymentEventTable {
    @PrimaryKey
    @Id
    private String eventId;
    private PaymentEventType eventType;
    private String paymentId;
    private LocalDateTime timestamp;
    @Column(length = 1024)
    private String eventData;
}
