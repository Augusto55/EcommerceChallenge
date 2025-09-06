package br.com.ecommercechallenge.service;

import br.com.ecommercechallenge.dto.product.ProductCreateDto;
import br.com.ecommercechallenge.model.Product;
import br.com.ecommercechallenge.repository.ProductOrderRepository;
import br.com.ecommercechallenge.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductOrderRepository productOrderRepository;

    @InjectMocks
    private ProductService productService;

    private Product mockProduct;
    private ProductCreateDto mockProductCreateDto;
    private UUID mockProductId;

    @BeforeEach
    void setUp() {
        mockProductId = UUID.randomUUID();
        mockProduct = Product.builder()
                .id(mockProductId)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(99.99))
                .quantity(100)
                .active(true)
                .createdAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)))
                .build();

        mockProductCreateDto = new ProductCreateDto(
                "Test Product",
                "Test Description",
                BigDecimal.valueOf(99.99),
                100,
                true
        );
    }

    @Test
    @DisplayName("Should list all products with pagination")
    void testListAllProducts() {
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(mockProduct));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        Page<Product> result = productService.listAllProducts(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(mockProduct, result.getContent().get(0));
        verify(productRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("Should find product by valid ID")
    void testFindProductById_Success() {
        when(productRepository.findById(mockProductId)).thenReturn(Optional.of(mockProduct));

        Product result = productService.findProductById(mockProductId.toString());

        assertNotNull(result);
        assertEquals(mockProduct, result);
        verify(productRepository).findById(mockProductId);
    }


    @Test
    @DisplayName("Should create product successfully")
    void testCreateProduct_Success() {
        when(productRepository.findByName(mockProductCreateDto.name())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        Product result = productService.createProduct(mockProductCreateDto);

        assertNotNull(result);
        assertEquals(mockProduct, result);
        verify(productRepository).findByName(mockProductCreateDto.name());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should delete product successfully when not associated with orders")
    void testDeleteProductById_Success() {
        when(productRepository.findById(mockProductId)).thenReturn(Optional.of(mockProduct));
        when(productOrderRepository.findByProduct(mockProduct)).thenReturn(Optional.empty());

        productService.deleteProductById(mockProductId.toString());

        verify(productRepository).findById(mockProductId);
        verify(productOrderRepository).findByProduct(mockProduct);
        verify(productRepository).delete(mockProduct);
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProduct_Success() {
        ProductCreateDto updateDto = new ProductCreateDto(
                "Updated Product",
                "Updated Description",
                BigDecimal.valueOf(149.99),
                200,
                false
        );
        
        when(productRepository.findById(mockProductId)).thenReturn(Optional.of(mockProduct));
        when(productRepository.findByName(updateDto.name())).thenReturn(Optional.empty());

        productService.updateProduct(mockProductId.toString(), updateDto);

        verify(productRepository).findById(mockProductId);
        verify(productRepository).findByName(updateDto.name());
        verify(productRepository).save(mockProduct);
        assertEquals("Updated Product", mockProduct.getName());
        assertEquals("Updated Description", mockProduct.getDescription());
        assertEquals(BigDecimal.valueOf(149.99), mockProduct.getPrice());
        assertEquals(200, mockProduct.getQuantity());
        assertFalse(mockProduct.getActive());
    }

    @Test
    @DisplayName("Should update product active status successfully")
    void testUpdateProductActive_Success() {
        when(productRepository.findById(mockProductId)).thenReturn(Optional.of(mockProduct));

        productService.updateProductActive(mockProductId.toString(), false);

        verify(productRepository).findById(mockProductId);
        verify(productRepository).save(mockProduct);
        assertFalse(mockProduct.getActive());
        assertNotNull(mockProduct.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle updating product active status to same value")
    void testUpdateProductActive_SameValue() {
        when(productRepository.findById(mockProductId)).thenReturn(Optional.of(mockProduct));

        productService.updateProductActive(mockProductId.toString(), true);

        verify(productRepository).findById(mockProductId);
        verify(productRepository).save(mockProduct);
        assertTrue(mockProduct.getActive());
        assertNotNull(mockProduct.getUpdatedAt());
    }
} 