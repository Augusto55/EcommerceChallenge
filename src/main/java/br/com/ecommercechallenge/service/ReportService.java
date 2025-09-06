package br.com.ecommercechallenge.service;

import br.com.ecommercechallenge.dto.product.ProductResponseDto;
import br.com.ecommercechallenge.dto.product.ProductSalesReportDto;
import br.com.ecommercechallenge.dto.report.SalesReportDto;
import br.com.ecommercechallenge.dto.report.TopSellingProductsDto;
import br.com.ecommercechallenge.dto.user.UserOrderCountDto;
import br.com.ecommercechallenge.mapper.ProductMapper;
import br.com.ecommercechallenge.repository.OrderRepository;
import br.com.ecommercechallenge.repository.ProductOrderRepository;
import br.com.ecommercechallenge.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.ByteArrayOutputStream;


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

    public byte[] generateSimplePdf() throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            PdfContentByte canvas = writer.getDirectContent();

            // Cabeçalho com retângulo
            Rectangle headerRect = new Rectangle(36, 770, 559, 820); // margem inferior esquerda (x,y) até superior direita
            headerRect.setBorder(Rectangle.BOX);
            headerRect.setBorderWidth(2);
            canvas.rectangle(headerRect);

            // Texto dentro do cabeçalho
            ColumnText.showTextAligned(
                    canvas, Element.ALIGN_LEFT, new Phrase("Relatório de Exemplo - Cabeçalho"),
                    50, 795, 0
            );

            // Corpo com retângulo
            Rectangle bodyRect = new Rectangle(36, 100, 559, 750);
            bodyRect.setBorder(Rectangle.BOX);
            bodyRect.setBorderWidth(1);
            canvas.rectangle(bodyRect);

            // Texto dentro do corpo
            ColumnText.showTextAligned(
                    canvas, Element.ALIGN_LEFT, new Phrase("Este é o corpo do PDF."),
                    50, 730, 0
            );

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }
}
