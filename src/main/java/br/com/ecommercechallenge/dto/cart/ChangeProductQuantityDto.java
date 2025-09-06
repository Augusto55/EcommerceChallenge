package br.com.ecommercechallenge.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ChangeProductQuantityDto(
        @NotNull @PositiveOrZero Integer quantity
) {
}
