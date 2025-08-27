package com.magabyzr.ecommercemg.repositories;

import com.magabyzr.ecommercemg.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}