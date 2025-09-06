package br.com.ecommercechallenge.controller;

import br.com.ecommercechallenge.config.SecurityConfig;
import br.com.ecommercechallenge.dto.ResponseMessageDto;
import br.com.ecommercechallenge.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Tag(name = "Cart", description = "Requests for placing an order")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAuthority('SCOPE_Default') or #userId == authentication.name")
    @PostMapping("/placeOrder")
    public ResponseEntity<ResponseMessageDto> changeCartProductQuantity(Authentication authentication){
        String userid = authentication.getName();
        orderService.placeOrder(userid);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessageDto("Order placed successfully"));
    }
}
