package br.com.compass.ecommercechallenge.dto.report;

import br.com.compass.ecommercechallenge.dto.product.ProductSalesReportDto;

public record TopSellingProductsDto(
        ProductSalesReportDto day,
        ProductSalesReportDto week,
        ProductSalesReportDto year
) {

}
