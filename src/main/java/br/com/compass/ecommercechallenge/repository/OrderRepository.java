package br.com.compass.ecommercechallenge.repository;

import br.com.compass.ecommercechallenge.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
