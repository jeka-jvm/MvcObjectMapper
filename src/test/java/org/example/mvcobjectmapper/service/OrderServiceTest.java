package org.example.mvcobjectmapper.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.mvcobjectmapper.entity.Order;
import org.example.mvcobjectmapper.entity.Product;
import org.example.mvcobjectmapper.exception.ResourceNotFoundException;
import org.example.mvcobjectmapper.repository.OrderRepository;
import org.example.mvcobjectmapper.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() {
        Order order1 = new Order(1L, null, null, LocalDateTime.now(), "Address 1", BigDecimal.TEN, "Shipped");
        Order order2 = new Order(2L, null, null, LocalDateTime.now(), "Address 2", BigDecimal.valueOf(20), "Delivered");

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getAllOrders();

        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void createOrder_shouldCalculateTotalPriceAndSaveOrder() {
        Product product1 = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(100), 2);
        Product product2 = new Product(2L, "Product 2", "Description 2", BigDecimal.valueOf(200), 1);
        Order order = new Order();
        order.setProducts(Arrays.asList(product1, product2));

        when(productRepository.findProductForUpdate(1L)).thenReturn(product1);
        when(productRepository.findProductForUpdate(2L)).thenReturn(product2);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.createOrder(order);

        assertEquals(BigDecimal.valueOf(400), savedOrder.getTotalPrice());
        verify(orderRepository).save(order);
    }

    @Test
    void getOrderById_shouldReturnOrder_whenOrderExists() {
        Order order = new Order();
        order.setOrderId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(1L);

        assertEquals(order, foundOrder);
    }

    @Test
    void getOrderById_shouldThrowException_whenOrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }
}