package com.example.springcoffeeshop.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.cglib.core.Local;

import com.example.springcoffeeshop.dto.ProductRequestDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private Integer priceInCents;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted = false;

    public Product(ProductRequestDTO request) {
        this.name = request.name();
        this.description = request.description();
        this.priceInCents = request.priceInCents();
        this.isAvailable = request.isAvailable();

        LocalDateTime currenTime = LocalDateTime.now();
        this.createdAt = currenTime;
        this.updatedAt = currenTime;
    }
}
