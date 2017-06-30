package com.github.sandokandias.payments.interfaces.request;

import com.github.sandokandias.payments.domain.vo.Transaction;
import lombok.Data;

@Data
public class PerformPaymentRequest {
    private String accountId;
    private String intent;
    private String paymentMethod;
    private Transaction transaction;
}
