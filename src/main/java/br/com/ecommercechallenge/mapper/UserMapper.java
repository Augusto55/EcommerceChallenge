package br.com.ecommercechallenge.mapper;

import br.com.ecommercechallenge.dto.user.UserResponseDto;
import br.com.ecommercechallenge.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper{
    UserResponseDto userToUserResponseDto(User user);
    User userResponseDtoToUser(UserResponseDto userResponseDto);
}
