package br.com.ecommercechallenge.dto.report;

import br.com.ecommercechallenge.dto.product.ProductSalesReportDto;

public record TopSellingProductsDto(
        ProductSalesReportDto day,
        ProductSalesReportDto week,
        ProductSalesReportDto year
) {

}
