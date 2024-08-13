package org.example.mvcobjectmapper.service;

import jakarta.transaction.Transactional;
import org.example.mvcobjectmapper.entity.Order;
import org.example.mvcobjectmapper.entity.Product;
import org.example.mvcobjectmapper.exception.ResourceNotFoundException;
import org.example.mvcobjectmapper.repository.OrderRepository;
import org.example.mvcobjectmapper.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class OrderService {


    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order createOrder(Order order) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Product product : order.getProducts()) {
            Product lockedProduct = productRepository.findProductForUpdate(product.getProductId());
            BigDecimal productTotal = lockedProduct.getPrice().multiply(BigDecimal.valueOf(lockedProduct.getQuantityInStock()));
            totalPrice = totalPrice.add(productTotal);
        }

        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                              .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }
}
