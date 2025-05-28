package br.com.compass.ecommercechallenge.repository;

import br.com.compass.ecommercechallenge.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartProductRepository extends JpaRepository<CartProduct, UUID> {
}
