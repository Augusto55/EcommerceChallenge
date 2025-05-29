package br.com.compass.ecommercechallenge.mapper;

import br.com.compass.ecommercechallenge.dto.cart.CartProductDto;
import br.com.compass.ecommercechallenge.dto.cart.ProductInCartDto;
import br.com.compass.ecommercechallenge.model.CartProduct;
import br.com.compass.ecommercechallenge.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartProductMapper {
    CartProductDto cartProductToCardProductDto(CartProduct cartProduct);
    ProductInCartDto productToProductInCartDto(Product product);
}
