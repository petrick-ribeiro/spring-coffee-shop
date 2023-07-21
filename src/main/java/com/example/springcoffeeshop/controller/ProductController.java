package com.example.springcoffeeshop.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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
import com.example.springcoffeeshop.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/product")
public class ProductController {
    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Product>> handleGetProduct() {
        return ResponseEntity.ok(service.listAllProducts());
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> handleGetProductByID(@PathVariable @Valid UUID id) {
        Product isProductExists = service.findProductByID(id);
        if (isProductExists == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(isProductExists);
    }

    @PostMapping
    public ResponseEntity<Product> handlePostProduct(@RequestBody @Valid ProductRequestDTO data) {
        Product newProduct = service.createNewProduct(data);

        return ResponseEntity.created(URI.create("/product/" + newProduct.getId())).body(newProduct);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> handlePutProductByID(@PathVariable @Valid UUID id,
            @RequestBody @Valid ProductRequestDTO data) {
        Product updatedProduct = service.updateProductByID(id, data);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Product> handleDeleteProductByID(@PathVariable @Valid UUID id) {
        Product removedProduct = service.removeProductByID(id);
        if (removedProduct == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}