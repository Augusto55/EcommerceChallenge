package br.com.ecommercechallenge.dto.cart;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponseDto(
        UUID id,
        List<CartProductDto> productList,
        BigDecimal totalPrice) {
}
