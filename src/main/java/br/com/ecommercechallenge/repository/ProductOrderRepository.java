package br.com.ecommercechallenge.repository;

import br.com.ecommercechallenge.dto.product.ProductSalesReportDto;
import br.com.ecommercechallenge.model.Product;
import br.com.ecommercechallenge.model.ProductOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, UUID> {
    Optional<List<ProductOrder>> findByProduct(Product product);

    @Query("""
    SELECT new br.com.ecommercechallenge.dto.product.ProductSalesReportDto(
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
