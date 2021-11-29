package org.example.service.impl;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.entity.UserEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        List<UserEntity> userEntityList = new ArrayList<>(userRepository.findAll());
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        Stream<UserEntity> stream = userEntityList.stream();
        stream.forEach(userEntity -> userResponseDtoList.add(userMapper.UserEntityToUserResponseDto(userEntity)));
        return userResponseDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        return userMapper.UserEntityToUserResponseDto(
                userRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find user with id = %d", id)
                        )
                )
        );
    }

    @Override
    @Transactional
    public UserResponseDto add(UserRequestDto userRequestDto) {
        UserEntity userEntity = userMapper.UserRequestDtoToUserEntity(userRequestDto);
        userRepository.save(userEntity);
        return userMapper.UserEntityToUserResponseDto(userEntity);
    }

    @Override
    @Transactional
    public UserResponseDto change(Long id, UserRequestDto userRequestDto) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find user with id = %d", id)
                )
        );
        if (userRequestDto.getName() != null) userEntity.setName(userRequestDto.getName());
        if (userRequestDto.getPriority() != null) userEntity.setPriority(userRequestDto.getPriority());
        return userMapper.UserEntityToUserResponseDto(userEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find user with id = %d", id)
                )
        );
        userRepository.delete(userEntity);
    }
}
