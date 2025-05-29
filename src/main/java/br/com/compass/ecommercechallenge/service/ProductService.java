package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.dto.product.ProductCreateDto;
import br.com.compass.ecommercechallenge.exception.InvalidUuidFormatException;
import br.com.compass.ecommercechallenge.exception.NotFoundException;
import br.com.compass.ecommercechallenge.exception.ProductAssociatedWithOrderException;
import br.com.compass.ecommercechallenge.exception.ResourceAlreadyExistsException;
import br.com.compass.ecommercechallenge.model.Product;
import br.com.compass.ecommercechallenge.repository.ProductOrderRepository;
import br.com.compass.ecommercechallenge.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;

    public ProductService(ProductRepository productRepository, ProductOrderRepository productOrderRepository) {
        this.productRepository = productRepository;
        this.productOrderRepository = productOrderRepository;
    }

    public Page<Product> listAllProducts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return productRepository.findAll(pageable);
    }

    public Product findProductById(String productId) {
        UUID productUuid;
        try{
            productUuid = UUID.fromString(productId);
        } catch (IllegalArgumentException e) {
            throw new InvalidUuidFormatException();
        }

        return productRepository.findById(productUuid)
                .orElseThrow(() -> new NotFoundException("Product"));
    }


    @Transactional
    public Product createProduct(ProductCreateDto product) {
        var productWithName = productRepository.findByName(product.name());
        if (productWithName.isPresent()) {
            throw new ResourceAlreadyExistsException("Product", "name" );
        }

        Product newProduct = Product.builder()
                .name(product.name())
                .description(product.description())
                .price(product.price())
                .quantity(product.quantity())
                .active(product.active())
                .createdAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)))
                .build();

        return productRepository.save(newProduct);
    }



    @Transactional
    public void deleteProductById(String productId) {
        var product = findProductById(productId);
        if (product == null) {
            throw new NotFoundException("Product");
        }
        var productOrder = productOrderRepository.findByProduct(product);
        if(productOrder.isPresent()) { throw new ProductAssociatedWithOrderException(productId); }

        productRepository.delete(product);
    }

    @Transactional
    public void updateProduct(String productId, ProductCreateDto productDto) {
        var product = this.findProductById(productId);

        var productWithName = productRepository.findByName(productDto.name());
        if (productWithName.isPresent()) {
            throw new ResourceAlreadyExistsException("Product", "name" );
        }

        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setQuantity(productDto.quantity());
        product.setActive(productDto.active());
        product.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));

        productRepository.save(product);
    }

    @Transactional
    public void updateProductActive(String productId, boolean active) {
        var product = findProductById(productId);

        product.setActive(active);
        product.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        productRepository.save(product);
    }

}
