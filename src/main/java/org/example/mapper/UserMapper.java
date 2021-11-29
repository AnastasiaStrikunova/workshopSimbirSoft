package org.example.mapper;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserEntity UserRequestDtoToUserEntity (UserRequestDto userRequestDto);
    UserResponseDto UserEntityToUserResponseDto (UserEntity userEntity);
}
