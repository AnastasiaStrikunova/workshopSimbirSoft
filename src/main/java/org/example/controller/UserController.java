package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.UserRequestDto;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Пользователи", description = "Управление пользователями")
@RestController
@RequestMapping("/user")
public class UserController {
    //private final UserService userService;

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить конкретного пользователя")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить пользователя")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Изменить пользователя")
    @PutMapping("/{id}")
    public ResponseEntity<Object> change(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){}
}
