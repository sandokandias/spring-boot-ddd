package com.github.sandokandias.payments.domain.vo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class MoneyTest {

    @Test
    public void shouldSerializeAsJson() throws IOException {

        Money money = new Money("BRL", 200, 2);
        String asJson = new ObjectMapper().writeValueAsString(money);
        String expected = "{\"currency\":\"BRL\",\"amount\":200,\"scale\":2}";

        Assert.assertEquals(expected, asJson);
    }

    @Test
    public void shouldDeserializeAsMoney() throws IOException {

        String asJson = "{\"currency\":\"BRL\",\"amount\":200,\"scale\":2}";
        Money money = new ObjectMapper().readValue(asJson, Money.class);
        Money expected = new Money("BRL", 200, 2);

        Assert.assertTrue(expected.sameValueAs(money));
    }

}
