package br.com.compass.ecommercechallenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
