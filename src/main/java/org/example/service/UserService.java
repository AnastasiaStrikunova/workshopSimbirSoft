package org.example.service;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;

import java.util.List;

/**
 * Этот интерфейс указывает какие методы необходимы для реализации бизнес-логики <i>пользователей</i>
 * @author Anastasia Strikunova
 */
public interface UserService {
    List<UserResponseDto> findAll();
    UserResponseDto findById(Long id);
    UserResponseDto add(UserRequestDto userRequestDto);
    UserResponseDto change(Long id, UserRequestDto userRequestDto);
    void delete(Long id);
}
