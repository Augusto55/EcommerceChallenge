package br.com.compass.ecommercechallenge.repository;

import br.com.compass.ecommercechallenge.dto.product.ProductSalesReportDto;
import br.com.compass.ecommercechallenge.model.Product;
import br.com.compass.ecommercechallenge.model.ProductOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, UUID> {
    Optional<ProductOrder> findByProduct(Product product);

    @Query("""
    SELECT new br.com.compass.ecommercechallenge.dto.product.ProductSalesReportDto(
        po.product.id,
        po.product.name,
        po.product.description,
        po.product.price,    
        SUM(po.quantity)
    )
    FROM ProductOrder po
    GROUP BY po.product.id, po.product.description, po.product.price, po.product.name
    ORDER BY SUM(po.quantity) DESC
    """)
    List<ProductSalesReportDto> getTopSellingProducts(Pageable pageable);

    @Query("SELECT SUM(po.unitPrice * po.quantity) FROM ProductOrder po")
    BigDecimal getTotalSalesValue();
}
