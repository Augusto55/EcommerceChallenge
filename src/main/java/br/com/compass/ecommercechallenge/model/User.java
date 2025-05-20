package br.com.compass.ecommercechallenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;
    String password;
    String email;
    Boolean active;
    UserTypeEnum userType;
    Timestamp createdAt;
    Timestamp updatedAt;
    @OneToMany(mappedBy = "user")
    List<Order> orders;
    @OneToOne
    @JoinColumn(name = "cart_id")
    Cart shoppingCart;
}
