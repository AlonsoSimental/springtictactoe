package com.tictactoe.springtictactoe.controllers;

import com.tictactoe.springtictactoe.domain.dto.UserDto;
import com.tictactoe.springtictactoe.domain.entities.UserEntity;
import com.tictactoe.springtictactoe.mapper.Mapper;
import com.tictactoe.springtictactoe.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private UserService userService;
    private Mapper mapper;

    public UserController(UserService userService, Mapper mapper) {

        this.userService = userService;
        this.mapper = mapper;

    }

    @PostMapping(path = "/users") //Post para crear un usuario nuevo
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        Logger logger = LoggerFactory.getLogger(UserController.class);
        try {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(userDto.getUsername());
            newUser.setPassword(userDto.getPassword());
            UserEntity savedUser = userService.createUser(newUser);
            logger.info("User created succesfully: "+savedUser.getUsername());
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAllUsers().stream()
                .map(user -> mapper.userToDto(user))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(user -> ResponseEntity.ok(mapper.userToDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        System.out.println("Received ID: " +id);
        System.out.println("Received DTO: "+ userDto);

        userDto.setUserId(id);
        UserEntity userEntity = mapper.userToEntity(userDto);
        UserEntity updatedUser = userService.updateUser(userEntity);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.userToDto(updatedUser));
    }

    @PatchMapping(path = "/users/{id}")
    public ResponseEntity<UserEntity> updatePartialUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        UserEntity updatedUser = userService.updatePartialUser(id, updates);
        if(updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable Long id) {
        Optional<UserEntity> userOptional = userService.findUserById(id);
        if(!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
