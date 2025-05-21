package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.dto.UserCreateDto;
import br.com.compass.ecommercechallenge.model.User;
import br.com.compass.ecommercechallenge.model.UserTypeEnum;
import br.com.compass.ecommercechallenge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Iterable<User> listAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void createUser(UserCreateDto user) {
        try {
            User newUser = User.builder()
                    .name(user.name())
                    .password(passwordEncoder.encode(user.password()))
                    .email(user.email())
                    .active(true)
                    .userType(UserTypeEnum.DEFAULT)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)))
                    .build();

            userRepository.save(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
