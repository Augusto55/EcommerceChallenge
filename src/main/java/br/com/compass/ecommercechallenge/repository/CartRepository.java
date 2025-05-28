package br.com.compass.ecommercechallenge.repository;

import br.com.compass.ecommercechallenge.model.Cart;
import br.com.compass.ecommercechallenge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser_Id(UUID userId);
}
