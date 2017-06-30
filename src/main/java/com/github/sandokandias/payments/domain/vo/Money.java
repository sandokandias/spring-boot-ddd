package com.github.sandokandias.payments.domain.vo;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sandokandias.payments.domain.shared.ValueObject;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Currency;

@EqualsAndHashCode(exclude = {"amountAsBigDecimal"})
public class Money implements ValueObject<Money> {
    @NotNull
    @Size(min = 3, max = 3)
    public final String currency;
    @NotNull
    public final Integer amount;
    @JsonIgnore
    public final BigDecimal amountAsBigDecimal;
    @NotNull
    public final Integer precision;

    @JsonCreator
    public Money(@JsonProperty("currency") String currency,
                 @JsonProperty("amount") Integer amount,
                 @JsonProperty("precision") Integer precision) {
        this.currency = Currency.getInstance(currency).getCurrencyCode();
        this.amount = amount;
        this.precision = precision;
        this.amountAsBigDecimal = new BigDecimal(amount).movePointLeft(precision);
    }

    @Override
    public boolean sameValueAs(Money other) {
        return other != null &&
                this.currency.equals(other.currency) &&
                this.amount.equals(other.amount) &&
                this.precision.equals(other.precision);
    }
}
