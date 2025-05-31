package br.com.compass.ecommercechallenge.dto.user;

import br.com.compass.ecommercechallenge.model.Cart;
import br.com.compass.ecommercechallenge.model.Order;
import br.com.compass.ecommercechallenge.model.UserTypeEnum;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record UserResponseDto(UUID id, String name, String email,
                              Boolean active,
                              UserTypeEnum userType,
                              Timestamp createdAt,
                              Timestamp updatedAt,
                              List<Order> orders) {
}
