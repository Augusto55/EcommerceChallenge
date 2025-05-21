package br.com.compass.ecommercechallenge.controller;

import br.com.compass.ecommercechallenge.dto.UserCreateDto;
import br.com.compass.ecommercechallenge.model.User;
import br.com.compass.ecommercechallenge.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Iterable<User> listAllUsers(){
        return userService.listAllUsers();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateDto user){
        userService.createUser(user);
        return ResponseEntity.ok().build();
    }

}
