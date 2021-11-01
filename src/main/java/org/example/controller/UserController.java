package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.UserRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Пользователи", description = "Управление пользователями")
@RestController
public class UserController {
    //private final UserServiceImpl userService;

    @Operation(summary = "Получить всех пользователей")
    @GetMapping("/user")
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить конкретного пользователя")
    @GetMapping("/user/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить пользователя")
    @PostMapping("/user")
    public ResponseEntity<Object> add(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Изменить пользователя")
    @PutMapping("/user/{id}")
    public ResponseEntity<Object> change(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }
}
