package com.github.sandokandias.payments.domain.vo;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sandokandias.payments.domain.shared.ValueObject;
import com.github.sandokandias.payments.infrastructure.util.validation.ValidEnum;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

@EqualsAndHashCode(exclude = {"amountAsBigDecimal"})
public class Money implements ValueObject<Money> {

    @ValidEnum(conformsTo = CurrencyCodes.class)
    public final String currency;
    @NotNull
    public final Integer amount;
    @NotNull
    public final Integer scale;
    @JsonIgnore
    public final BigDecimal amountAsBigDecimal;


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Money(@JsonProperty("currency") String currency,
                 @JsonProperty("amount") Integer amount,
                 @JsonProperty("scale") Integer scale) {
        this.currency = Currency.getInstance(currency).getCurrencyCode();
        this.amount = amount;
        this.scale = scale;
        this.amountAsBigDecimal = new BigDecimal(amount).movePointLeft(2).setScale(scale);
    }

    @Override
    public boolean sameValueAs(Money other) {
        return other != null &&
                this.currency.equals(other.currency) &&
                this.amount.equals(other.amount) &&
                this.scale.equals(other.scale);
    }
}
