package br.com.compass.ecommercechallenge.model;

import br.com.compass.ecommercechallenge.dto.LoginRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    //@JdbcTypeCode(SqlTypes.CHAR)
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

    public boolean validateLoginCredentials(LoginRequestDto loginRequestDto, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequestDto.password(), this.password);
    }
}
