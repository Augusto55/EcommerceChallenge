package br.com.ecommercechallenge.repository;

import br.com.ecommercechallenge.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    void deleteAllByUserId(UUID userId);
}
