package br.com.compass.ecommercechallenge.repository;

import br.com.compass.ecommercechallenge.dto.user.UserOrderCountDto;
import br.com.compass.ecommercechallenge.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("""
    SELECT new br.com.compass.ecommercechallenge.dto.user.UserOrderCountDto(
        o.user.id,
        o.user.name,
        o.user.email,
        COUNT(o)
    )
    FROM Order o
    GROUP BY o.user.id, o.user.name, o.user.email
    ORDER BY COUNT(o) DESC
    """)
    List<UserOrderCountDto> findUsersByOrderCount();

}
