package br.com.ecommercechallenge.controller;


import br.com.ecommercechallenge.dto.ResponseMessageDto;
import br.com.ecommercechallenge.dto.auth.EmailRequestDto;
import br.com.ecommercechallenge.dto.auth.LoginRequestDto;
import br.com.ecommercechallenge.dto.auth.LoginResponseDto;
import br.com.ecommercechallenge.dto.auth.ResetPasswordDto;
import br.com.ecommercechallenge.service.AuthenticationService;
import br.com.ecommercechallenge.service.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Requests related to login, password and authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    public AuthenticationController(AuthenticationService authenticationService, EmailService emailService) {
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        var response = authenticationService.authenticate(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recover-password")
    public ResponseEntity<?> recoverPassword(@RequestBody @Valid EmailRequestDto emailRequestDto) {
        var token = authenticationService.generatePasswordResetToken(emailRequestDto.email());
        emailService.sendPasswordResetEmail(emailRequestDto.email(), token.getToken().toString());
        return ResponseEntity.ok(new ResponseMessageDto("Password reset email has been sent and should arrive within a few minutes."));
    }


    @PostMapping("/create-new-password")
    public ResponseEntity<?> createNewPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto) {
        authenticationService.validateResetPasswordToken(resetPasswordDto.token(), resetPasswordDto.password());
        return ResponseEntity.ok(new ResponseMessageDto("Password successfully changed. You can now log in."));
    }
}
