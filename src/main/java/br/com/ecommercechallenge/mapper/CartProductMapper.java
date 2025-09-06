package br.com.ecommercechallenge.mapper;

import br.com.ecommercechallenge.dto.cart.CartProductDto;
import br.com.ecommercechallenge.dto.cart.ProductInCartDto;
import br.com.ecommercechallenge.model.CartProduct;
import br.com.ecommercechallenge.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartProductMapper {
    CartProductDto cartProductToCardProductDto(CartProduct cartProduct);
    ProductInCartDto productToProductInCartDto(Product product);
}
