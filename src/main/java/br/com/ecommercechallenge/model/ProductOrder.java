package br.com.ecommercechallenge.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_order")
public class ProductOrder {

    @Id @GeneratedValue
    UUID id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
    Integer quantity;
    BigDecimal unitPrice;

}