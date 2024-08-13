package org.example.mvcobjectmapper.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.mvcobjectmapper.entity.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;


class OrderJsonUtilTest {

    @Test
    void toJson_shouldConvertOrderToJson() {
        Order order = new Order(1L, null, Collections.emptyList(), LocalDateTime.now(), "Address 1", BigDecimal.TEN, "Shipped");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = OrderJsonUtil.toJson(order);

        assertDoesNotThrow(() -> objectMapper.readTree(json));
    }

    @Test
    void fromJson_shouldConvertJsonToOrder() {
        String json = "{\"orderId\":1,\"shippingAddress\":\"Address 1\",\"totalPrice\":10,\"orderStatus\":\"Shipped\"}";

        Order order = OrderJsonUtil.fromJson(json);

        assertNotNull(order);
        assertEquals(1L, order.getOrderId());
        assertEquals("Address 1", order.getShippingAddress());
    }

    @Test
    void toJson_shouldThrowRuntimeException_whenConversionFails() throws JsonProcessingException {
        Order order = new Order();
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);

        doThrow(JsonProcessingException.class).when(objectMapperMock).writeValueAsString(order);

    }

    @Test
    void fromJson_shouldThrowRuntimeException_whenConversionFails() {
        String invalidJson = "invalid_json";

        assertThrows(RuntimeException.class, () -> OrderJsonUtil.fromJson(invalidJson));
    }
}