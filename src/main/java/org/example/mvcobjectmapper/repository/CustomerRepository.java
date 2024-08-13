package org.example.mvcobjectmapper.repository;

import org.example.mvcobjectmapper.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
