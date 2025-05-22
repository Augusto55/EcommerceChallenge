package br.com.compass.ecommercechallenge.controller;

import br.com.compass.ecommercechallenge.dto.UserCreateDto;
import br.com.compass.ecommercechallenge.dto.UserCreationSuccessDto;
import br.com.compass.ecommercechallenge.dto.UserResponseDto;
import br.com.compass.ecommercechallenge.mapper.UserMapper;
import br.com.compass.ecommercechallenge.model.User;
import br.com.compass.ecommercechallenge.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserMapper userMapper;
    UserService userService;

    UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserResponseDto> listAllUsers(){
        var userList = userService.listAllUsers();

        return userList.stream()
                .map(user -> userMapper.userToUserResponseDto(user))
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<UserCreationSuccessDto> createUser(@RequestBody UserCreateDto user){
        var createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserCreationSuccessDto("User successfully created", createdUser.getId().toString()));
    }

}
