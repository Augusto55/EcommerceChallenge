package br.com.ecommercechallenge.repository;

import br.com.ecommercechallenge.dto.product.ProductSalesReportDto;
import br.com.ecommercechallenge.dto.user.UserOrderCountDto;
import br.com.ecommercechallenge.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("""
    SELECT new br.com.ecommercechallenge.dto.user.UserOrderCountDto(
        o.user.id,
        o.user.name,
        o.user.email,
        COUNT(o)
    )
    FROM Order o
    GROUP BY o.user.id, o.user.name, o.user.email
    ORDER BY COUNT(o) DESC
    """)
    List<UserOrderCountDto> findUsersByOrderCount(Pageable pageable);

    @Query("""
    SELECT new br.com.ecommercechallenge.dto.product.ProductSalesReportDto(
        p.id,
        p.name,
        p.description,
        p.price,
        SUM(po.quantity)
    )
    FROM ProductOrder po
    JOIN po.product p
    JOIN po.order o
    WHERE o.createdAt BETWEEN :startDate AND :endDate
    GROUP BY p.id, p.name, p.description, p.price
    ORDER BY SUM(po.quantity) DESC
""")
    List<ProductSalesReportDto> findTopSellingProductsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );



}
