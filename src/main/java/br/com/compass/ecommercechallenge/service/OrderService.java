package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.exception.EmptyCartException;
import br.com.compass.ecommercechallenge.exception.InactivateProductException;
import br.com.compass.ecommercechallenge.exception.InsufficientStockException;
import br.com.compass.ecommercechallenge.exception.NotFoundException;
import br.com.compass.ecommercechallenge.model.Order;
import br.com.compass.ecommercechallenge.model.ProductOrder;
import br.com.compass.ecommercechallenge.repository.CartProductRepository;
import br.com.compass.ecommercechallenge.repository.CartRepository;
import br.com.compass.ecommercechallenge.repository.OrderRepository;
import br.com.compass.ecommercechallenge.repository.ProductOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final ProductOrderRepository productOrderRepository;
    private final OrderRepository orderRepository;
    private final CartProductRepository cartProductRepository;

    public OrderService(CartRepository cartRepository, ProductOrderRepository productOrderRepository, OrderRepository orderRepository, CartProductRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.productOrderRepository = productOrderRepository;
        this.orderRepository = orderRepository;
        this.cartProductRepository = cartProductRepository;
    }

    @Transactional
    public void placeOrder(String userId) {
        var cart = cartRepository.findByUser_Id(UUID.fromString(userId))
                .orElseThrow(() -> new NotFoundException("Cart"));
        if (cart.getProductList().isEmpty()) {
            throw new EmptyCartException();
        }

        var user = cart.getUser();

        var order = new Order();
        order.setUser(user);
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));

        BigDecimal totalPrice = cart.getProductList().stream()
                .map(cp -> cp.getProduct().getPrice().multiply(BigDecimal.valueOf(cp.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);

        List<ProductOrder> productOrderList = new ArrayList<>();

        cart.getProductList().forEach(cartProduct -> {

            if (cartProduct.getProduct().getQuantity() < cartProduct.getQuantity()) throw new InsufficientStockException();

            if (!cartProduct.getProduct().getActive()) throw new InactivateProductException();

            var productOrder = new ProductOrder();
            productOrder.setProduct(cartProduct.getProduct());
            productOrder.setQuantity(cartProduct.getQuantity());
            productOrder.setOrder(order);
            productOrder.setUnitPrice(cartProduct.getProduct().getPrice());

            productOrderList.add(productOrder);
            productOrderRepository.save(productOrder);

            cartProduct.getProduct().setQuantity(cartProduct.getProduct().getQuantity() - cartProduct.getQuantity());
        });

        cart.getProductList().clear();
        cartRepository.save(cart);

        order.setItemList(productOrderList);
        orderRepository.save(order);
    }
}
