package br.com.ecommercechallenge.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSalesReportDto(
        UUID id,
        String productName,
        String description,
        BigDecimal price,
        Long totalSold
) {
}
