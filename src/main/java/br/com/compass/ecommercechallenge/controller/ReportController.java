package br.com.compass.ecommercechallenge.controller;

import br.com.compass.ecommercechallenge.config.SecurityConfig;
import br.com.compass.ecommercechallenge.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/reports")
@Tag(name = "Report", description = "Requests that allow administrators to view reports")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/salesReport")
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<?> getSalesReport() {
        var fullReport = reportService.getSalesReport();
        return ResponseEntity.ok(fullReport);
    }

    @GetMapping("/lowStock")
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<?> getLowStockReport() {
        var lowStockReport = reportService.getLowStockReport();
        return ResponseEntity.ok(lowStockReport);
    }

    @GetMapping("/mostSold")
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<?> getMostSoldProductsReport() {
        var topSoldList = reportService.getMostSoldProductsReport();
        return ResponseEntity.ok().body(topSoldList);
    }

    @GetMapping("/topBuyers")
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<?> getUsersWithMostOrders() {
        var topBuyers = reportService.getUsersWithMostOrdersReport();
        return ResponseEntity.ok().body(topBuyers);
    }
}
