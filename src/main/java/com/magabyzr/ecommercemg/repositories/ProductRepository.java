package com.magabyzr.ecommercemg.repositories;

import com.magabyzr.ecommercemg.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}