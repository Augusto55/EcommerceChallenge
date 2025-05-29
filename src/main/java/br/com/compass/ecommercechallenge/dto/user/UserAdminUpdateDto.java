package br.com.compass.ecommercechallenge.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserAdminUpdateDto (
        @Size(min = 2, max = 100) String name,
        @Email @Size(max = 255) String email,
        Boolean isActive
){
}
