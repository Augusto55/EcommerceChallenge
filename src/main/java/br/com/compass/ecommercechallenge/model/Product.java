package br.com.compass.ecommercechallenge.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;
    String description;
    BigDecimal price;
    Integer quantity;
    Boolean active;
    Timestamp createdAt;
    Timestamp updatedAt;
}
