package com.magabyzr.ecommercemg.repositories;

import com.magabyzr.ecommercemg.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}