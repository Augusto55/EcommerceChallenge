package br.com.compass.ecommercechallenge.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record AddProductDto(
        @NotNull UUID productId,
        @NotNull @Positive Integer quantity
) {
}
