package com.example.springcoffeeshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequestDTO(
        @NotBlank String name,

        String description,

        @NotNull Integer priceInCents,

        @NotNull Boolean isAvailable) {

}
