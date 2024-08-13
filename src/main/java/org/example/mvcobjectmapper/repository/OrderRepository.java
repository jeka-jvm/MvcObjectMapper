package org.example.mvcobjectmapper.repository;

import org.example.mvcobjectmapper.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

}
