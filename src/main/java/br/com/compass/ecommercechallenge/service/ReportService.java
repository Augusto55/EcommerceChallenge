package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.dto.product.ProductResponseDto;
import br.com.compass.ecommercechallenge.dto.product.ProductSalesReportDto;
import br.com.compass.ecommercechallenge.dto.user.UserOrderCountDto;
import br.com.compass.ecommercechallenge.mapper.ProductMapper;
import br.com.compass.ecommercechallenge.repository.OrderRepository;
import br.com.compass.ecommercechallenge.repository.ProductOrderRepository;
import br.com.compass.ecommercechallenge.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ProductOrderRepository productOrderRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ProductMapper productMapper;

    public ReportService(ProductOrderRepository productOrderRepository, ProductRepository productRepository, OrderRepository orderRepository, ProductMapper productMapper) {
        this.productOrderRepository = productOrderRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponseDto> getLowStockReport(){
        var products = productRepository.findAllByQuantityLessThan(50);

        return products.stream()
                        .map(productMapper::productToProductResponseDto)
                        .collect(Collectors.toList());
    }

    public List<ProductSalesReportDto> getMostSoldProductsReport(){
        var top10Selling = productOrderRepository.getTopSellingProducts()
                .stream()
                .limit(10)
                .collect(Collectors.toList());

        return top10Selling;
    }

    public List<UserOrderCountDto> getUsersWithMostOrdersReport(){
        var top10Buyers = orderRepository.findUsersByOrderCount()
                .stream()
                .limit(10)
                .collect(Collectors.toList());

        return top10Buyers;
    }
}
