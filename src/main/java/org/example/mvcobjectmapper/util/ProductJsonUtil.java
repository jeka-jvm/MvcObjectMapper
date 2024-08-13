package org.example.mvcobjectmapper.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.mvcobjectmapper.entity.Product;


public class ProductJsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void setObjectMapper(ObjectMapper mapper) {
        objectMapper = mapper;
    }

    public static String toJson(Product product) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert Product to JSON", e);
        }
    }

    public static Product fromJson(String json) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(json, Product.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to Product", e);
        }
    }
}
