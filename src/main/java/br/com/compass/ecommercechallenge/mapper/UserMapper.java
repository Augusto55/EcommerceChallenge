package br.com.compass.ecommercechallenge.mapper;

import br.com.compass.ecommercechallenge.dto.UserResponseDto;
import br.com.compass.ecommercechallenge.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper{
    UserResponseDto userToUserResponseDto(User user);
    User userResponseDtoToUser(UserResponseDto userResponseDto);
}
