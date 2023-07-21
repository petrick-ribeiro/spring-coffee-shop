package com.example.springcoffeeshop.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springcoffeeshop.model.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Soft delete a record by ID given
    // @Transactional
    // @Modifying
    // @Query("UPDATE Product p SET p.deletedAt = :deletedAt, p.isDeleted = true
    // WHERE p.id = :id")
    // Product softDeleteById(UUID id, LocalDateTime deletedAt);

    // Find all records where ID is false
    List<Product> findByIsDeletedFalse(Sort sort);
}