package org.example.mvcobjectmapper.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mvcobjectmapper.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ProductJsonUtilTest {

    @BeforeEach
    void setUp() {
        ProductJsonUtil.setObjectMapper(new ObjectMapper());
    }

    @AfterEach
    void tearDown() {
        ProductJsonUtil.setObjectMapper(new ObjectMapper());
    }

    @Test
    void toJson_shouldThrowRuntimeExceptionWhenJsonProcessingFails() {
        ObjectMapper faultyMapper = new ObjectMapper() {
            @Override
            public String writeValueAsString(Object value) throws JsonProcessingException {
                throw new JsonProcessingException("Simulated JSON processing error") {};
            }
        };

        ProductJsonUtil.setObjectMapper(faultyMapper);
        Product product = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(99.99), 10);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ProductJsonUtil.toJson(product);
        });

        assertEquals("Failed to convert Product to JSON", exception.getMessage());
    }

    @Test
    void fromJson_shouldThrowRuntimeExceptionWhenJsonProcessingFails() {
        ObjectMapper faultMapper = new ObjectMapper() {
            @Override
            public <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException {
                throw new JsonProcessingException("Simulated JSON processing error") {};
            }
        };

        ProductJsonUtil.setObjectMapper(faultMapper);
        String faultJson = "invalid json";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ProductJsonUtil.fromJson(faultJson);
        });

        assertEquals("Failed to convert JSON to Product", exception.getMessage());
    }
}