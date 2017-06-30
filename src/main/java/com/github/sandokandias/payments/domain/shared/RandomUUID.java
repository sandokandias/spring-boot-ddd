package com.github.sandokandias.payments.domain.shared;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public abstract class RandomUUID implements ValueObject<RandomUUID> {

    @NotNull
    @Size(min = 16, max = 50)
    public final String id;

    public RandomUUID() {
        this.id = String.format(getPrefix(), UUID.randomUUID().toString());
    }

    public RandomUUID(String id) {
        this.id = id;
    }

    @Override
    public boolean sameValueAs(RandomUUID other) {
        return other != null && this.id.equals(other.id);
    }

    protected abstract String getPrefix();
}
