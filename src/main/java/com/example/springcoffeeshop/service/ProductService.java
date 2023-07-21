package com.example.springcoffeeshop.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.springcoffeeshop.dto.ProductRequestDTO;
import com.example.springcoffeeshop.model.Product;
import com.example.springcoffeeshop.repository.ProductRepository;

@Service
public class ProductService {
    private ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> listAllProducts() {
        return repository.findByIsDeletedFalse(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Product findProductByID(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public Product createNewProduct(ProductRequestDTO product) {
        Product newProduct = new Product(product);

        return repository.save(newProduct);
    }

    public Product updateProductByID(UUID id, ProductRequestDTO product) {
        Product productToUpdate = findProductByID(id);
        if (productToUpdate != null) {
            productToUpdate.setName(product.name());
            productToUpdate.setDescription(product.description());
            productToUpdate.setPriceInCents(product.priceInCents());
            productToUpdate.setIsAvailable(product.isAvailable());
            productToUpdate.setUpdatedAt(LocalDateTime.now());

            return repository.save(productToUpdate);
        }

        return productToUpdate;
    }

    public Product removeProductByID(UUID id) {
        Product productToRemove = findProductByID(id);
        if (productToRemove != null) {
            productToRemove.setDeletedAt(LocalDateTime.now());
            productToRemove.setIsDeleted(true);

            repository.save(productToRemove);

            return productToRemove;
        }

        return productToRemove;
    }
}
