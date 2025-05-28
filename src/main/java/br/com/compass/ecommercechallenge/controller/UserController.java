package br.com.compass.ecommercechallenge.controller;

import br.com.compass.ecommercechallenge.dto.ResourceCreationSuccessDto;
import br.com.compass.ecommercechallenge.dto.ResponseMessageDto;
import br.com.compass.ecommercechallenge.dto.user.*;
import br.com.compass.ecommercechallenge.mapper.UserMapper;
import br.com.compass.ecommercechallenge.model.UserTypeEnum;
import br.com.compass.ecommercechallenge.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserMapper userMapper;
    UserService userService;

    UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_Administrator') or #userId == authentication.name")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String userId){
        var user = userService.findUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userMapper.userToUserResponseDto(user));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<Page<UserResponseDto>> listAllUsers( @RequestParam(defaultValue = "0") int pageNumber,
                                                    @RequestParam(defaultValue = "10") int pageSize){
        var userList = userService.listAllUsers(pageNumber, pageSize);
        Page<UserResponseDto> dtoPage = userList.map(
                user -> userMapper.userToUserResponseDto(user));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoPage);
    }

    @PostMapping("/default")
    public ResponseEntity<ResourceCreationSuccessDto> createDefaultUser(@RequestBody @Valid UserCreateDto user){
        var createdUser = userService.createUser(user, UserTypeEnum.DEFAULT);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResourceCreationSuccessDto("User successfully created", createdUser.getId().toString()));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<ResourceCreationSuccessDto> createAdminUser(@RequestBody @Valid UserCreateDto user){
        var createdUser = userService.createUser(user, UserTypeEnum.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResourceCreationSuccessDto("User successfully created", createdUser.getId().toString()));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<ResponseMessageDto> deleteUser(@PathVariable String userId){
        userService.deleteUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseMessageDto("User successfully deleted."));
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_Administrator') or #userId == authentication.name")
    public ResponseEntity<ResponseMessageDto> updateUser(@PathVariable String userId,
                                                         @RequestBody @Valid UserUpdateDto userUpdateDto) {
        userService.updateUser(userId, userUpdateDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseMessageDto("User successfully updated."));
    }


    @PatchMapping("/admin/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_Administrator')")
    public ResponseEntity<ResponseMessageDto> updateUserByAdmin(@PathVariable String userId,
                                                                @RequestBody UserAdminUpdateDto userAdminUpdateDto) {
        userService.updateAdminUser(userId, userAdminUpdateDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseMessageDto("User successfully updated."));
    }

}
