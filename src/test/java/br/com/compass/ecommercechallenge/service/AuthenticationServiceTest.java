package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.dto.auth.LoginRequestDto;
import br.com.compass.ecommercechallenge.dto.auth.LoginResponseDto;
import br.com.compass.ecommercechallenge.exception.InvalidCredentialsException;
import br.com.compass.ecommercechallenge.exception.InvalidTokenException;
import br.com.compass.ecommercechallenge.exception.NotFoundException;
import br.com.compass.ecommercechallenge.exception.SamePasswordException;
import br.com.compass.ecommercechallenge.model.PasswordResetToken;
import br.com.compass.ecommercechallenge.model.User;
import br.com.compass.ecommercechallenge.model.UserTypeEnum;
import br.com.compass.ecommercechallenge.repository.PasswordResetTokenRepository;
import br.com.compass.ecommercechallenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Authentication Service Tests")
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User mockUser;
    private LoginRequestDto mockLoginRequest;
    private UUID mockUserId;
    private PasswordResetToken mockPasswordResetToken;

    @BeforeEach
    void setUp() {
        mockUserId = UUID.randomUUID();
        mockUser = User.builder()
                .id(mockUserId)
                .name("John Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .active(true)
                .userType(UserTypeEnum.DEFAULT)
                .build();

        mockLoginRequest = new LoginRequestDto("john@example.com", "password123");
        
        mockPasswordResetToken = new PasswordResetToken(
                UUID.randomUUID(),
                mockUser,
                Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15)),
                false
        );
    }

    @Test
    @DisplayName("Should authenticate user successfully")
    void testAuthenticate_Success() {
        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("jwt-token-value");
        
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(mockUser.validateLoginCredentials(mockLoginRequest, passwordEncoder)).thenReturn(true);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        LoginResponseDto result = authenticationService.authenticate(mockLoginRequest);

        assertNotNull(result);
        assertEquals("jwt-token-value", result.accessToken());
        assertEquals(300L, result.expiresIn());
        verify(userRepository).findByEmail("john@example.com");
        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }

    @Test
    @DisplayName("Should generate password reset token successfully")
    void testGeneratePasswordResetToken_Success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(mockPasswordResetToken);

        PasswordResetToken result = authenticationService.generatePasswordResetToken("john@example.com");

        assertNotNull(result);
        assertEquals(mockPasswordResetToken, result);
        verify(userRepository).findByEmail("john@example.com");
        verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
    }

    @Test
    @DisplayName("Should validate reset password token successfully")
    void testValidateResetPasswordToken_Success() {
        String tokenId = mockPasswordResetToken.getToken().toString();
        String newPassword = "newPassword123";
        
        when(passwordResetTokenRepository.findById(UUID.fromString(tokenId)))
                .thenReturn(Optional.of(mockPasswordResetToken));
        when(passwordEncoder.matches(newPassword, "encodedPassword")).thenReturn(false);
        when(passwordEncoder.encode(newPassword)).thenReturn("newEncodedPassword");

        authenticationService.validateResetPasswordToken(tokenId, newPassword);

        verify(passwordResetTokenRepository).findById(UUID.fromString(tokenId));
        verify(passwordEncoder).matches(newPassword, "encodedPassword");
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(mockUser);
        verify(passwordResetTokenRepository).delete(mockPasswordResetToken);
    }
} 