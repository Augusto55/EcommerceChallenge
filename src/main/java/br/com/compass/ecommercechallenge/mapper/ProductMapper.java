package br.com.compass.ecommercechallenge.mapper;

import br.com.compass.ecommercechallenge.dto.product.ProductResponseDto;
import br.com.compass.ecommercechallenge.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface ProductMapper {
    ProductResponseDto productToProductResponseDto(Product product);
    Product productResponseDtoToProduct(ProductResponseDto productResponseDto);
}
