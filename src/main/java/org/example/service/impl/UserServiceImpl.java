package org.example.service.impl;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.entity.UserEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List findAll() {
        List userEntityList = new ArrayList(userRepository.findAll());
        List userResponseDtoList = new ArrayList();
        for (Object o : userEntityList) {
            userResponseDtoList.add(UserMapper.INSTANCE.UserEntityToUserResponseDto((UserEntity) o));
        }
        return userResponseDtoList;
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        return UserMapper.INSTANCE.UserEntityToUserResponseDto(
                userRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find object with id = %d",id)
                        )
                )
        );
    }

    @Transactional
    public UserResponseDto add(UserRequestDto userRequestDto) {
        UserEntity userEntity = UserMapper.INSTANCE.UserRequestDtoToUserEntity(userRequestDto);
        userRepository.save(userEntity);
        return UserMapper.INSTANCE.UserEntityToUserResponseDto(userEntity);
    }

    @Transactional
    public UserResponseDto change(Long id, UserRequestDto userRequestDto) {
        UserEntity entity = UserMapper.INSTANCE.UserRequestDtoToUserEntity(userRequestDto);
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find object with id = %d",id)
                )
        );
        if (entity.getName() != null) userEntity.setName(entity.getName());
        if (entity.getPriority() != null) userEntity.setPriority(entity.getPriority());
        userRepository.save(userEntity);
        return UserMapper.INSTANCE.UserEntityToUserResponseDto(userEntity);
    }

    @Transactional
    public void delete(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find object with id = %d",id)
                )
        );
        userRepository.delete(userEntity);
    }
}
