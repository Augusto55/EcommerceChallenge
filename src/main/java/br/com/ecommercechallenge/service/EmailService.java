package br.com.ecommercechallenge.service;

import br.com.ecommercechallenge.repository.PasswordResetTokenRepository;
import br.com.ecommercechallenge.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender mailSender;

    public EmailService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailSender = mailSender;
    }

    @Async
    public void sendPasswordResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Token");
        message.setText("We received a request to reset your password. " +
                "Please use the token below to proceed with the password reset process:\n" +
                "\n" +
                "Token: " + token);
        message.setFrom("ecommerce@challenge.com");

        mailSender.send(message);
    }

}
