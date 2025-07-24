package com.sellout.productservice.repository;

import com.sellout.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // You can later add queries like: findByIsActiveTrue(), findBySellerId(Long id), etc.
}
