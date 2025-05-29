package br.com.compass.ecommercechallenge.repository;

import br.com.compass.ecommercechallenge.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByName(String productName);
    List<Product> findAllByQuantityLessThan(int quantity);
}
