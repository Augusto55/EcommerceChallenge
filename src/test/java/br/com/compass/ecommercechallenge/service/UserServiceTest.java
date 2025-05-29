package br.com.compass.ecommercechallenge.service;

import br.com.compass.ecommercechallenge.dto.user.UserAdminUpdateDto;
import br.com.compass.ecommercechallenge.dto.user.UserCreateDto;
import br.com.compass.ecommercechallenge.dto.user.UserUpdateDto;
import br.com.compass.ecommercechallenge.exception.InvalidUuidFormatException;
import br.com.compass.ecommercechallenge.exception.NotFoundException;
import br.com.compass.ecommercechallenge.exception.ResourceAlreadyExistsException;
import br.com.compass.ecommercechallenge.exception.SamePasswordException;
import br.com.compass.ecommercechallenge.model.Cart;
import br.com.compass.ecommercechallenge.model.User;
import br.com.compass.ecommercechallenge.model.UserTypeEnum;
import br.com.compass.ecommercechallenge.repository.CartRepository;
import br.com.compass.ecommercechallenge.repository.PasswordResetTokenRepository;
import br.com.compass.ecommercechallenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;
    private UserCreateDto mockUserCreateDto;
    private UUID mockUserId;

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
                .createdAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)))
                .build();
        
        mockUserCreateDto = new UserCreateDto("John Doe", "john@example.com", "password123");
    }

    @Test
    @DisplayName("Should list all users with pagination")
    void testListAllUsers() {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(mockUser));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<User> result = userService.listAllUsers(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(mockUser, result.getContent().get(0));
        verify(userRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("Should find user by valid ID")
    void testFindUserById_Success() {
        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));

        User result = userService.findUserById(mockUserId.toString());

        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(userRepository).findById(mockUserId);
    }


    @Test
    @DisplayName("Should find user by email")
    void testFindUserByEmail() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.findUserByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());
        verify(userRepository).findByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should create user successfully")
    void testCreateUser_Success() {
        Cart mockCart = new Cart();
        mockCart.setId(UUID.randomUUID());
        
        when(userRepository.findByEmail(mockUserCreateDto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(mockUserCreateDto.password())).thenReturn("encodedPassword");
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.createUser(mockUserCreateDto, UserTypeEnum.DEFAULT);

        assertNotNull(result);
        verify(userRepository).findByEmail(mockUserCreateDto.email());
        verify(passwordEncoder).encode(mockUserCreateDto.password());
        verify(cartRepository).save(any(Cart.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user by ID successfully")
    void testDeleteUserById_Success() {
        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));

        userService.deleteUserById(mockUserId.toString());

        verify(userRepository).findById(mockUserId);
        verify(passwordResetTokenRepository).deleteAllByUserId(mockUserId);
        verify(userRepository).delete(mockUser);
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUser_Success() {
        UserUpdateDto updateDto = new UserUpdateDto("New Name", "newPassword");
        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("newPassword", "encodedPassword")).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");

        userService.updateUser(mockUserId.toString(), updateDto);

        verify(userRepository).findById(mockUserId);
        verify(passwordEncoder).matches("newPassword", "encodedPassword");
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(mockUser);
    }

    @Test
    @DisplayName("Should update admin user successfully")
    void testUpdateAdminUser_Success() {
        UserAdminUpdateDto adminUpdateDto = new UserAdminUpdateDto("New Name", "new@email.com", false);
        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));

        userService.updateAdminUser(mockUserId.toString(), adminUpdateDto);

        verify(userRepository).findById(mockUserId);
        verify(userRepository).save(mockUser);
        assertEquals("New Name", mockUser.getName());
        assertEquals("new@email.com", mockUser.getEmail());
        assertFalse(mockUser.getActive());
    }

    @Test
    @DisplayName("Should update admin user with partial data")
    void testUpdateAdminUser_PartialUpdate() {
        UserAdminUpdateDto adminUpdateDto = new UserAdminUpdateDto("New Name", null, null);
        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));

        userService.updateAdminUser(mockUserId.toString(), adminUpdateDto);

        verify(userRepository).findById(mockUserId);
        verify(userRepository).save(mockUser);
        assertEquals("New Name", mockUser.getName());
        assertEquals("john@example.com", mockUser.getEmail());
        assertTrue(mockUser.getActive());
    }
} 