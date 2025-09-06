package br.com.ecommercechallenge.service;

import br.com.ecommercechallenge.dto.product.ProductResponseDto;
import br.com.ecommercechallenge.dto.report.SalesReportDto;
import br.com.ecommercechallenge.mapper.ProductMapper;
import br.com.ecommercechallenge.model.Product;
import br.com.ecommercechallenge.repository.OrderRepository;
import br.com.ecommercechallenge.repository.ProductOrderRepository;
import br.com.ecommercechallenge.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Report Service Tests - Reporting Domain")
class ReportServiceTest {

    @Mock
    private ProductOrderRepository productOrderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ReportService reportService;

    private Product mockProduct;
    private ProductResponseDto mockProductResponseDto;

    @BeforeEach
    void setUp() {
        mockProduct = Product.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(99.99))
                .quantity(30) // Low stock
                .active(true)
                .build();

        mockProductResponseDto = new ProductResponseDto(
                mockProduct.getId(),
                "Test Product",
                "Description",
                BigDecimal.valueOf(99.99),
                30,
                true
        );
    }

    @Test
    @DisplayName("Should get low stock report successfully")
    void testGetLowStockReport() {
        List<Product> lowStockProducts = Collections.singletonList(mockProduct);
        when(productRepository.findAllByQuantityLessThan(50)).thenReturn(lowStockProducts);
        when(productMapper.productToProductResponseDto(mockProduct)).thenReturn(mockProductResponseDto);

        List<ProductResponseDto> result = reportService.getLowStockReport();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockProductResponseDto, result.get(0));
        verify(productRepository).findAllByQuantityLessThan(50);
        verify(productMapper).productToProductResponseDto(mockProduct);
    }

    @Test
    @DisplayName("Should get sales report successfully")
    void testGetSalesReport() {
        when(orderRepository.count()).thenReturn(100L);
        when(productOrderRepository.getTotalSalesValue()).thenReturn(BigDecimal.valueOf(10000.00));
        when(orderRepository.findTopSellingProductsBetweenDates(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        SalesReportDto result = reportService.getSalesReport();

        assertNotNull(result);
        assertEquals(100L, result.totalSales());
        assertEquals(BigDecimal.valueOf(10000.00), result.totalSalesValue());
        verify(orderRepository).count();
        verify(productOrderRepository).getTotalSalesValue();
        verify(orderRepository, times(3)).findTopSellingProductsBetweenDates(any(), any(), any());
    }
} 