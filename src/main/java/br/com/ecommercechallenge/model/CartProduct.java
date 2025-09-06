package br.com.ecommercechallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_product")
public class CartProduct {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    Cart cart;
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
    Integer quantity;
}
