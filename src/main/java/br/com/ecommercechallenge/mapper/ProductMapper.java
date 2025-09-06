package br.com.ecommercechallenge.mapper;

import br.com.ecommercechallenge.dto.product.ProductResponseDto;
import br.com.ecommercechallenge.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface ProductMapper {
    ProductResponseDto productToProductResponseDto(Product product);
    Product productResponseDtoToProduct(ProductResponseDto productResponseDto);
}
