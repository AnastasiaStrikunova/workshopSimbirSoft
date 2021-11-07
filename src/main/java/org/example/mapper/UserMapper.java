package org.example.mapper;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserEntity UserRequestDtoToUserEntity (UserRequestDto userRequestDto);
    UserResponseDto UserEntityToUserResponseDto (UserEntity userEntity);
}
