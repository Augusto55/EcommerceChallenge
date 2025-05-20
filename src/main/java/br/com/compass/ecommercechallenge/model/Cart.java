package br.com.compass.ecommercechallenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
    @OneToMany(mappedBy = "cart")
    List<CartProduct> productList = new ArrayList<>();
    Timestamp createdAt;
    Timestamp updatedAt;
}
