package org.example.mvcobjectmapper.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.mvcobjectmapper.entity.Order;
import org.example.mvcobjectmapper.service.OrderService;
import org.example.mvcobjectmapper.util.OrderJsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void getAllOrders_shouldReturnJsonList() throws Exception {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 8, 13, 2, 36, 14, 741000000);
        Order order1 = new Order(1L, null, null, fixedTime, "Address 1", BigDecimal.TEN, "Shipped");
        Order order2 = new Order(2L, null, null, fixedTime, "Address 2", BigDecimal.valueOf(20), "Delivered");

        List<Order> orders = Arrays.asList(order1, order2);
        when(orderService.getAllOrders()).thenReturn(orders);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        List<String> expectedJsonOrders = orders.stream()
                .map(OrderJsonUtil::toJson)
                .collect(Collectors.toList());

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedJsonOrders)));
    }

    @Test
    void createOrder_shouldReturnSavedOrder() throws Exception {
        Order order = new Order(1L, null, null, LocalDateTime.now(), "Address 1", BigDecimal.TEN, "Shipped");
        String orderJson = OrderJsonUtil.toJson(order);

        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(orderJson));
    }

    @Test
    void getOrderById_shouldReturnOrder_whenOrderExists() throws Exception {
        Order order = new Order(1L, null, null, LocalDateTime.now(), "Address 1", BigDecimal.TEN, "Shipped");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String orderJson = objectMapper.writeValueAsString(order);

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(orderJson));
    }

    @Test
    void deleteOrder_shouldReturnOk_whenOrderExists() throws Exception {
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk());

        verify(orderService).deleteOrder(1L);
    }
}