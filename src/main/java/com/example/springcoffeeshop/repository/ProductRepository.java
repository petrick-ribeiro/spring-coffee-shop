package com.example.springcoffeeshop.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springcoffeeshop.model.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}