package br.com.compass.ecommercechallenge.dto.cart;

import java.util.UUID;

public record CartProductDto(
        UUID id,
        ProductInCartDto product,
        Integer quantity
) {
}
