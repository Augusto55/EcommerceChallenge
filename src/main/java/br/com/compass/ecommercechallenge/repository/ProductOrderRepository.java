package br.com.compass.ecommercechallenge.repository;

import br.com.compass.ecommercechallenge.model.Product;
import br.com.compass.ecommercechallenge.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, UUID> {
    Optional<ProductOrder> findByProduct(Product product);
}
