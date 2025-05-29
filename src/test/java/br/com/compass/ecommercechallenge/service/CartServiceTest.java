package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.dto.cart.AddProductDto;
import br.com.compass.ecommercechallenge.dto.cart.CartResponseDto;
import br.com.compass.ecommercechallenge.mapper.CartProductMapper;
import br.com.compass.ecommercechallenge.model.Cart;
import br.com.compass.ecommercechallenge.model.CartProduct;
import br.com.compass.ecommercechallenge.model.Product;
import br.com.compass.ecommercechallenge.model.User;
import br.com.compass.ecommercechallenge.repository.CartProductRepository;
import br.com.compass.ecommercechallenge.repository.CartRepository;
import br.com.compass.ecommercechallenge.repository.ProductRepository;
import br.com.compass.ecommercechallenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cart Service Tests - Shopping Domain")
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartProductRepository cartProductRepository;

    @Mock
    private CartProductMapper cartProductMapper;

    @InjectMocks
    private CartService cartService;

    private Cart mockCart;
    private Product mockProduct;
    private CartProduct mockCartProduct;
    private UUID mockUserId;

    @BeforeEach
    void setUp() {
        mockUserId = UUID.randomUUID();
        
        mockProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .price(BigDecimal.valueOf(99.99))
                .quantity(100)
                .active(true)
                .build();

        mockCart = new Cart();
        mockCart.setId(UUID.randomUUID());
        mockCart.setProductList(new ArrayList<>());

        mockCartProduct = new CartProduct();
        mockCartProduct.setProduct(mockProduct);
        mockCartProduct.setQuantity(2);
        mockCartProduct.setCart(mockCart);
    }

    @Test
    @DisplayName("Should get cart for current user successfully")
    void testGetCartForCurrentUser() {
        when(cartRepository.findByUser_Id(mockUserId)).thenReturn(Optional.of(mockCart));

        CartResponseDto result = cartService.getCartForCurrentUser(mockUserId.toString());

        assertNotNull(result);
        verify(cartRepository).findByUser_Id(mockUserId);
    }

    @Test
    @DisplayName("Should add product to cart successfully")
    void testAddProductToCart() {
        AddProductDto addProductDto = new AddProductDto(mockProduct.getId(), 2);
        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));
        when(cartRepository.findByUser_Id(mockUserId)).thenReturn(Optional.of(mockCart));
        when(cartProductRepository.save(any(CartProduct.class))).thenReturn(mockCartProduct);

        CartProduct result = cartService.addProductToCart(mockUserId.toString(), addProductDto);

        assertNotNull(result);
        verify(productRepository).findById(mockProduct.getId());
        verify(cartRepository).findByUser_Id(mockUserId);
        verify(cartProductRepository).save(any(CartProduct.class));
    }

    @Test
    @DisplayName("Should remove product from cart successfully")
    void testRemoveProductFromCart() {
        String productId = mockProduct.getId().toString();
        when(cartRepository.findByUser_Id(mockUserId)).thenReturn(Optional.of(mockCart));
        when(cartProductRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockCartProduct));

        cartService.removeProductFromCart(mockUserId.toString(), productId);

        verify(cartRepository).findByUser_Id(mockUserId);
        verify(cartProductRepository).findById(mockProduct.getId());
        verify(cartProductRepository).delete(mockCartProduct);
    }

    @Test
    @DisplayName("Should change product quantity successfully")
    void testChangeProductQuantity() {
        String productId = mockProduct.getId().toString();
        int newQuantity = 5;
        when(cartRepository.findByUser_Id(mockUserId)).thenReturn(Optional.of(mockCart));
        when(cartProductRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockCartProduct));

        cartService.changeProductQuantity(mockUserId.toString(), productId, newQuantity);

        verify(cartRepository).findByUser_Id(mockUserId);
        verify(cartProductRepository).findById(mockProduct.getId());
        verify(cartProductRepository).save(mockCartProduct);
        assertEquals(newQuantity, mockCartProduct.getQuantity());
    }
} 