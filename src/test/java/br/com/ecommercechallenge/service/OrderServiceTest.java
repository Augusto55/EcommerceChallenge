package br.com.ecommercechallenge.service;

import br.com.ecommercechallenge.model.Cart;
import br.com.ecommercechallenge.model.CartProduct;
import br.com.ecommercechallenge.model.Order;
import br.com.ecommercechallenge.model.Product;
import br.com.ecommercechallenge.model.ProductOrder;
import br.com.ecommercechallenge.model.User;
import br.com.ecommercechallenge.model.UserTypeEnum;
import br.com.ecommercechallenge.repository.CartProductRepository;
import br.com.ecommercechallenge.repository.CartRepository;
import br.com.ecommercechallenge.repository.OrderRepository;
import br.com.ecommercechallenge.repository.ProductOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Order Service Tests - Shopping Domain")
class OrderServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductOrderRepository productOrderRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartProductRepository cartProductRepository;

    @InjectMocks
    private OrderService orderService;

    private Cart mockCart;
    private User mockUser;
    private Product mockProduct;
    private CartProduct mockCartProduct;
    private UUID mockUserId;

    @BeforeEach
    void setUp() {
        mockUserId = UUID.randomUUID();
        
        mockUser = User.builder()
                .id(mockUserId)
                .name("John Doe")
                .email("john@example.com")
                .userType(UserTypeEnum.DEFAULT)
                .build();

        mockProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .price(BigDecimal.valueOf(99.99))
                .quantity(100)
                .active(true)
                .build();

        mockCartProduct = new CartProduct();
        mockCartProduct.setProduct(mockProduct);
        mockCartProduct.setQuantity(2);

        List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(mockCartProduct);

        mockCart = new Cart();
        mockCart.setId(UUID.randomUUID());
        mockCart.setUser(mockUser);
        mockCart.setProductList(cartProducts);
    }

    @Test
    @DisplayName("Should place order successfully")
    void testPlaceOrder() {
        when(cartRepository.findByUser_Id(mockUserId)).thenReturn(Optional.of(mockCart));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());
        when(productOrderRepository.save(any(ProductOrder.class))).thenReturn(new ProductOrder());
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        orderService.placeOrder(mockUserId.toString());

        verify(cartRepository).findByUser_Id(mockUserId);
        verify(orderRepository).save(any(Order.class));
        verify(productOrderRepository).save(any(ProductOrder.class));
        verify(cartRepository).save(mockCart);
        
        assertTrue(mockCart.getProductList().isEmpty());
        
        assertEquals(98, mockProduct.getQuantity()); // 100 - 2
    }
} 