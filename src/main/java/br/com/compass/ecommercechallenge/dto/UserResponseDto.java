package br.com.compass.ecommercechallenge.dto;

import br.com.compass.ecommercechallenge.model.Cart;
import br.com.compass.ecommercechallenge.model.Order;

import java.util.List;
import java.util.UUID;

public record UserResponseDto(UUID id, String name, String email,
                              List<Order> orders,
                              Cart shoppingCart) {
}
