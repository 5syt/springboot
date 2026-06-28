package com.houserental.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.math.BigDecimal;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(BigDecimal.class, new JsonDeserializer<BigDecimal>() {
            @Override
            public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                if (value == null || value.trim().isEmpty()) {
                    return null;
                }
                return new BigDecimal(value.trim());
            }
        });

        module.addDeserializer(Long.class, new JsonDeserializer<Long>() {
            @Override
            public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                if (value == null || value.trim().isEmpty()) {
                    return null;
                }
                return Long.valueOf(value.trim());
            }
        });

        module.addDeserializer(Integer.class, new JsonDeserializer<Integer>() {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                if (value == null || value.trim().isEmpty()) {
                    return null;
                }
                return Integer.valueOf(value.trim());
            }
        });

        module.addDeserializer(Double.class, new JsonDeserializer<Double>() {
            @Override
            public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                if (value == null || value.trim().isEmpty()) {
                    return null;
                }
                return Double.valueOf(value.trim());
            }
        });

        objectMapper.registerModule(module);
        return objectMapper;
    }

}
