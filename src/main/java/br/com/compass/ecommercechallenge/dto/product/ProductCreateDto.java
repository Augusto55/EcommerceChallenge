package br.com.compass.ecommercechallenge.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateDto(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull @DecimalMin(value="0.0") BigDecimal price,
        @PositiveOrZero Integer quantity,
        @NotNull Boolean active
) {
}
