package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.dto.product.ProductResponseDto;
import br.com.compass.ecommercechallenge.dto.product.ProductSalesReportDto;
import br.com.compass.ecommercechallenge.dto.report.SalesReportDto;
import br.com.compass.ecommercechallenge.dto.report.TopSellingProductsDto;
import br.com.compass.ecommercechallenge.dto.user.UserOrderCountDto;
import br.com.compass.ecommercechallenge.mapper.ProductMapper;
import br.com.compass.ecommercechallenge.repository.OrderRepository;
import br.com.compass.ecommercechallenge.repository.ProductOrderRepository;
import br.com.compass.ecommercechallenge.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        var top10Selling = productOrderRepository.getTopSellingProducts(PageRequest.of(0, 10));
        return top10Selling;
    }

    public List<UserOrderCountDto> getUsersWithMostOrdersReport(){
        var top10Buyers = orderRepository.findUsersByOrderCount(PageRequest.of(0, 10));
        return top10Buyers;
    }

    public SalesReportDto getSalesReport() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        var dayProducts = orderRepository.findTopSellingProductsBetweenDates(startOfDay, endOfDay, PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);

        LocalDateTime startOfWeek = now.toLocalDate().with(DayOfWeek.MONDAY)
                .atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);
        var weekProducts = orderRepository.findTopSellingProductsBetweenDates(startOfWeek, endOfWeek, PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);

        LocalDateTime startOfMonth = now.toLocalDate().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
        var monthProducts = orderRepository.findTopSellingProductsBetweenDates(startOfMonth, endOfMonth, PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);

        TopSellingProductsDto topSellingProductsDto = new TopSellingProductsDto(dayProducts, weekProducts, monthProducts);

        var totalSales = orderRepository.count();
        var totalSalesValue = productOrderRepository.getTotalSalesValue();

        return new SalesReportDto(totalSales, totalSalesValue, topSellingProductsDto);
    }
}
