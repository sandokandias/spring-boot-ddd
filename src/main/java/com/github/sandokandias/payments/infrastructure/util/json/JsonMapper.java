package com.github.sandokandias.payments.infrastructure.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class JsonMapper {

    private final ObjectMapper MAPPER;

    public JsonMapper() {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.registerModule(new Jdk8Module());
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public <T> T read(Object json, Class<T> tClass) {
        return MAPPER.convertValue(json, tClass);
    }

    public <T> String write(T model) {
        try {
            return MAPPER.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
