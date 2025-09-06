package br.com.ecommercechallenge.service;

import br.com.ecommercechallenge.repository.PasswordResetTokenRepository;
import br.com.ecommercechallenge.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Email Service Tests - Communication Domain")
class EmailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    @DisplayName("Should send password reset email successfully")
    void testSendPasswordResetEmail() {
        String email = "test@example.com";
        String token = "test-token-123";

        emailService.sendPasswordResetEmail(email, token);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
} 