package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.dto.user.UserAdminUpdateDto;
import br.com.compass.ecommercechallenge.dto.user.UserCreateDto;
import br.com.compass.ecommercechallenge.dto.user.UserUpdateDto;
import br.com.compass.ecommercechallenge.exception.*;
import br.com.compass.ecommercechallenge.model.Cart;
import br.com.compass.ecommercechallenge.model.User;
import br.com.compass.ecommercechallenge.model.UserTypeEnum;
import br.com.compass.ecommercechallenge.repository.CartRepository;
import br.com.compass.ecommercechallenge.repository.PasswordResetTokenRepository;
import br.com.compass.ecommercechallenge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.UnknownServiceException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final CartRepository cartRepository;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordResetTokenRepository passwordResetTokenRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.cartRepository = cartRepository;
    }


    public Page<User> listAllUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(pageable);
    }

    public User findUserById(String userId) {
        UUID userUuid;
        try{
            userUuid = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new InvalidUuidFormatException();
        }

        return userRepository.findById(userUuid)
                .orElseThrow(() -> new NotFoundException("User"));
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(UserCreateDto user, UserTypeEnum userType) {
        var userWithEmail = findUserByEmail(user.email());
        if (userWithEmail.isPresent()) {
            throw new ResourceAlreadyExistsException("User", "email" );
        }

        var now = (Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));

        User newUser = User.builder()
                .name(user.name())
                .password(passwordEncoder.encode(user.password()))
                .email(user.email())
                .active(true)
                .userType(userType)
                .createdAt(now)
                .build();

        var cart = cartRepository.save(new Cart(newUser, now));
        newUser.setShoppingCart(cart);

        return userRepository.save(newUser);
    }

    @Transactional
    public void deleteUserById(String userId) {
        var user = findUserById(userId);
        if (user == null) {
            throw new NotFoundException("User");
        }
        if(!user.getOrders().isEmpty()) {
            throw new UserWithOrdersAssociatedException();
        }

        passwordResetTokenRepository.deleteAllByUserId(user.getId());
        userRepository.delete(user);
    }

    @Transactional
    public void updateUser(String userId, UserUpdateDto userUpdateDto) {
        var user = this.findUserById(userId);
        if (userUpdateDto.password() != null && passwordEncoder.matches(userUpdateDto.password(), user.getPassword())) {
            throw new SamePasswordException();
        }

        if(userUpdateDto.name() != null){
            user.setName(userUpdateDto.name());
        }

        if(userUpdateDto.password() != null){
            user.setPassword(passwordEncoder.encode(userUpdateDto.password()));
        }

        user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        userRepository.save(user);
    }

    @Transactional
    public void updateAdminUser(String userId, UserAdminUpdateDto userAdminUpdateDto){
        var user = this.findUserById(userId);

        if(userAdminUpdateDto.name() != null){
            user.setName(userAdminUpdateDto.name());
        }

        if(userAdminUpdateDto.email() != null){
            user.setEmail(userAdminUpdateDto.email());
        }

        if(userAdminUpdateDto.isActive() != null){
            user.setActive(userAdminUpdateDto.isActive());
        }

        user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        userRepository.save(user);
    }

}
