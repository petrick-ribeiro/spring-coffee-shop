package com.example.springcoffeeshop.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcoffeeshop.dto.ProductRequestDTO;
import com.example.springcoffeeshop.model.Product;
import com.example.springcoffeeshop.repository.ProductRepository;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> allProducts = repository.findByIsDeletedFalse();

        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable @Valid UUID id) {
        Optional<Product> optProduct = repository.findById(id);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();

            return ResponseEntity.ok(product);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> createNewProduct(@RequestBody @Valid ProductRequestDTO data) {
        Product newProduct = new Product(data);
        Product savedProduct = repository.save(newProduct);

        return ResponseEntity.created(URI.create("/product/" + savedProduct.getId())).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody @Valid ProductRequestDTO data) {
        Optional<Product> optProduct = repository.findById(id);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            product.setName(data.name());
            product.setDescription(data.description());
            product.setPriceInCents(data.priceInCents());
            product.setIsAvailable(data.isAvailable());

            Product updatedProduct = repository.save(product);

            return ResponseEntity.ok(updatedProduct);
        }

        return ResponseEntity.notFound().build();
    }

    // TODO: Create Soft DELETE Method

}
