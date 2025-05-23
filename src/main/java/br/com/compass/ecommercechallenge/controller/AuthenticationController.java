package br.com.compass.ecommercechallenge.controller;

import br.com.compass.ecommercechallenge.dto.*;
import br.com.compass.ecommercechallenge.repository.UserRepository;
import br.com.compass.ecommercechallenge.service.AuthenticationService;
import br.com.compass.ecommercechallenge.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    public AuthenticationController(AuthenticationService authenticationService, EmailService emailService) {
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        var response = authenticationService.authenticate(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recover-password")
    public ResponseEntity<?> recoverPassword(@RequestBody EmailRequestDto emailRequestDto) {
        var token = authenticationService.generatePasswordResetToken(emailRequestDto.email());
        emailService.sendPasswordResetEmail(emailRequestDto.email(), token.getToken().toString());
        return ResponseEntity.ok(new ResponseMessageDto("Password reset email has been sent and should arrive within a few minutes."));
    }


    @PostMapping("/create-new-password")
    public ResponseEntity createNewPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        authenticationService.validateResetPasswordToken(resetPasswordDto.token(), resetPasswordDto.password());
        return ResponseEntity.ok(new ResponseMessageDto("Password successfully changed. You can now log in."));
    }
}
