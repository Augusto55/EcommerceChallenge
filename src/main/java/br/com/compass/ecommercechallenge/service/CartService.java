package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.dto.cart.AddProductDto;
import br.com.compass.ecommercechallenge.dto.cart.CartProductDto;
import br.com.compass.ecommercechallenge.dto.cart.CartResponseDto;
import br.com.compass.ecommercechallenge.exception.InactivateProductException;
import br.com.compass.ecommercechallenge.exception.InsufficientStockException;
import br.com.compass.ecommercechallenge.exception.InvalidUuidFormatException;
import br.com.compass.ecommercechallenge.exception.NotFoundException;
import br.com.compass.ecommercechallenge.mapper.CartProductMapper;
import br.com.compass.ecommercechallenge.model.Cart;
import br.com.compass.ecommercechallenge.model.CartProduct;
import br.com.compass.ecommercechallenge.model.User;
import br.com.compass.ecommercechallenge.repository.CartProductRepository;
import br.com.compass.ecommercechallenge.repository.CartRepository;
import br.com.compass.ecommercechallenge.repository.ProductRepository;
import br.com.compass.ecommercechallenge.repository.UserRepository;
import br.com.compass.ecommercechallenge.util.UuidUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final CartProductMapper cartProductMapper;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartProductRepository cartProductRepository, CartProductMapper cartProductMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartProductRepository = cartProductRepository;
        this.cartProductMapper = cartProductMapper;
    }

    private Cart getCartByUserId(String userId){
        return cartRepository.findByUser_Id(UUID.fromString(userId))
                .orElseThrow(() -> new NotFoundException("Cart"));
    }

    public CartResponseDto getCartForCurrentUser(String userId) {
        var cart = this.getCartByUserId(userId);

        var mappedCartProductDto = cart.getProductList().stream().map(
                        product -> cartProductMapper.cartProductToCardProductDto(product))
                .toList();

        var totalPriceSum =  cart.getProductList().stream().map(
                        p -> p.getProduct().getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDto(cart.getId(), mappedCartProductDto, totalPriceSum);
    }

    @Transactional
    public CartProduct addProductToCart(String userId, AddProductDto addProductDto) {
        var product = productRepository.findById(addProductDto.productId())
                .orElseThrow(() -> new NotFoundException("Product"));

        var cart = getCartByUserId(userId);

        CartProduct cartProduct = cart.getProductList().stream()
                .filter(cp -> cp.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);


        if (!product.getActive()) throw new InactivateProductException();

        int quantityToBeAdded = (cartProduct != null)
                ? cartProduct.getQuantity() + addProductDto.quantity()
                : addProductDto.quantity();

        if (product.getQuantity() < quantityToBeAdded) throw new InsufficientStockException();

        if(cartProduct != null){
            cartProduct.setQuantity(quantityToBeAdded);
            cartProductRepository.save(cartProduct);
        } else {
            var newCartProduct = new CartProduct();
            newCartProduct.setProduct(product);
            newCartProduct.setQuantity(addProductDto.quantity());
            newCartProduct.setCart(cart);
            cartProductRepository.save(newCartProduct);

            cart.getProductList().add(newCartProduct);

            cartRepository.save(cart);

            cartProduct = newCartProduct;
        }

        return cartProduct;
    }

    @Transactional
    public void removeProductFromCart(String userId, String productId) {
        getCartByUserId(userId);
        var productIdUuid = UuidUtil.validateStringToUUID(productId);

        var cartProduct = cartProductRepository.findById(productIdUuid)
                .orElseThrow(() -> new NotFoundException("Product"));

        cartProductRepository.delete(cartProduct);
    }

    @Transactional
    public void changeProductQuantity(String userId, String productId, int quantity) {
        getCartByUserId(userId);
        var productIdUuid = UuidUtil.validateStringToUUID(productId);

        var cartProduct = cartProductRepository.findById(productIdUuid)
                .orElseThrow(() -> new NotFoundException("Product"));

        if(quantity == 0){
            cartProductRepository.delete(cartProduct);
        }
        else {
            cartProduct.setQuantity(quantity);
            cartProductRepository.save(cartProduct);
        }

    }
}
