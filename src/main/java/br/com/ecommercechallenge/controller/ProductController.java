package br.com.ecommercechallenge.controller;

import br.com.ecommercechallenge.config.SecurityConfig;
import br.com.ecommercechallenge.dto.ResponseMessageDto;
import br.com.ecommercechallenge.dto.product.ProductActiveDto;
import br.com.ecommercechallenge.dto.product.ProductCreateDto;
import br.com.ecommercechallenge.dto.product.ProductResponseDto;
import br.com.ecommercechallenge.dto.ResourceCreationSuccessDto;
import br.com.ecommercechallenge.mapper.ProductMapper;
import br.com.ecommercechallenge.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "Requests related to managing product information")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;


    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable String productId){
        var product = productService.findProductById(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productMapper.productToProductResponseDto(product));
    }


    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> listAllProducts(@RequestParam(defaultValue = "0") int pageNumber,
                                                              @RequestParam(defaultValue = "10") int pageSize){
        var productList = productService.listAllProducts(pageNumber, pageSize);
        Page<ProductResponseDto> dtoPage = productList.map(
                product -> productMapper.productToProductResponseDto(product));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoPage);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<ResourceCreationSuccessDto> createProduct(@RequestBody @Valid ProductCreateDto product){
        var createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResourceCreationSuccessDto("Product successfully created", createdProduct.getId().toString()));
    }


    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<ResponseMessageDto> deleteProduct(@PathVariable String productId){
        productService.deleteProductById(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseMessageDto("Product successfully deleted."));
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<ResponseMessageDto> updateProduct(@PathVariable String productId,
                                                            @RequestBody @Valid ProductCreateDto productCreateDto) {
        productService.updateProduct(productId, productCreateDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseMessageDto("Product successfully updated."));
    }

    @PatchMapping("/{productId}/active")
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<ResponseMessageDto> updateProductActive(@PathVariable String productId,
                                                         @RequestBody @Valid ProductActiveDto activeDto) {
        productService.updateProductActive(productId, activeDto.active());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseMessageDto("Product successfully updated."));
    }

}
