package com.tictactoe.springtictactoe.services;

import com.tictactoe.springtictactoe.domain.entities.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    UserEntity createUser(UserEntity userEntity);
    List<UserEntity> findAllUsers();
    Optional<UserEntity> findUserById(Long id);
    UserEntity updateUser(UserEntity userEntity);
    UserEntity updatePartialUser(Long id, Map<String, Object> updates);
    void deleteUser(Long id);

}
