package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.dto.LoginRequestDto;
import br.com.compass.ecommercechallenge.dto.LoginResponseDto;
import br.com.compass.ecommercechallenge.model.PasswordResetToken;
import br.com.compass.ecommercechallenge.repository.PasswordResetTokenRepository;
import br.com.compass.ecommercechallenge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public LoginResponseDto authenticate(LoginRequestDto loginRequestDto){
        var user = userRepository.findByEmail(loginRequestDto.email());
        if (user.isEmpty() || !user.get().validateLoginCredentials(loginRequestDto, passwordEncoder)){
            throw new BadCredentialsException("Invalid email or password");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var scope = user.get().getUserType();

        var claims = JwtClaimsSet.builder()
                .issuer("ecommerce-challenge")
                .subject(user.get().getId().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .claim("scope", scope.label)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponseDto(jwtValue, expiresIn);
    }

    @Transactional
    public PasswordResetToken generatePasswordResetToken(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var token = UUID.randomUUID();

        var resetToken = new PasswordResetToken(token, user,
                Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15)), false);

        return passwordResetTokenRepository.save(resetToken);
    }

    @Transactional
    public void validateResetPasswordToken(String token, String newPassword) {
        var passwordReset = passwordResetTokenRepository.findById(UUID.fromString(token)).orElseThrow(
                () -> new RuntimeException("Invalid token.")
        );

        if (passwordReset.getExpiresAt().before(Timestamp.from(Instant.now()))) {
            throw new RuntimeException("Token is expired.");
        }

        var user = passwordReset.getUser();
        if (!passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new RuntimeException("Password cannot be the same.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(passwordReset);
    }
}
