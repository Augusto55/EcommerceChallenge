package br.com.compass.ecommercechallenge.dto.report;

import java.math.BigDecimal;

public record SalesReportDto(
        Long totalSales,
        BigDecimal totalSalesValue,
        TopSellingProductsDto topSellingProducts
) {
}
