package com.magabyzr.ecommercemg.repositories;

import com.magabyzr.ecommercemg.dtos.ProductSummaryDTO;
import com.magabyzr.ecommercemg.entities.Category;
import com.magabyzr.ecommercemg.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCriteriaRepository, JpaSpecificationExecutor<Product> {            //change from CrudRepository to JPARepository, to query by example.
  //Derived Query Methods for strings, ex.:
  List<Product> findByName(String name);
  List<Product> findByNameLike(String name);
  List<Product> findByNameNotLike(String name);
  List<Product> findByNameContaining(String name);
  List<Product> findByNameNotContaining(String name);
  List<Product> findByNameEndingWith(String name);
  List<Product> findByNameStartingWith(String name);

  //Derived Query Methods for numbers, ex.:
  List<Product> findByPrice(BigDecimal price);
  List<Product> findByPriceGreaterThan(BigDecimal price);
  List<Product> findByPriceGreaterThanEqual(BigDecimal price);
  List<Product> findByPriceLessThan(BigDecimal price);
  List<Product> findByPriceLessThanEqual(BigDecimal price);
  List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

  //Use the @Query
  //Find products whose prices are in a given range and sort by name.

  //@Query("select p from Product p where p.price between :min and :max order by p.name") replaced with the stored procedure.
  @Procedure("findProductsByPrice")
  List<Product> findProducts(BigDecimal min, BigDecimal max);

  @Query("select count(*) from Product p where p.price between :min and :max")
  long countProducts(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

  //To update prices.
  @Modifying
  @Query("update Product p set p.price = :newPrice where p.category.id = :categoryId")
  void updatePriceByCategory(BigDecimal newPrice, Byte categoryId);

  //Using Projections.
  List<ProductSummaryDTO> findByCategory(Category category);

  }