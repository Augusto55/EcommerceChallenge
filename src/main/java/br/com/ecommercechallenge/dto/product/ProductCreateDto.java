package br.com.ecommercechallenge.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateDto(
        @NotBlank @Size(min = 3) String name,
        @NotBlank @Size(min = 10) String description,
        @NotNull @DecimalMin(value="0.0") BigDecimal price,
        @PositiveOrZero Integer quantity,
        @NotNull Boolean active
) {
}
