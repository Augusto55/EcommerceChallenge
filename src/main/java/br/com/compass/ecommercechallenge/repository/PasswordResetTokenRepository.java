package br.com.compass.ecommercechallenge.repository;

import br.com.compass.ecommercechallenge.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
}
