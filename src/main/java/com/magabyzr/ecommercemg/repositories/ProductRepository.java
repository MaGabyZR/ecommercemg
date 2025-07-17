package com.magabyzr.ecommercemg.repositories;

import com.magabyzr.ecommercemg.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryId(Byte categoryId);

    // @Query("SELECT p FROM Product p JOIN FETCH p.category") //Option 1
    //Option 2
    @EntityGraph(attributePaths = "category")
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithCategory();
}