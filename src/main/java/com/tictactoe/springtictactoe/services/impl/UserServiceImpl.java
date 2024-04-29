package com.tictactoe.springtictactoe.services.impl;

import com.tictactoe.springtictactoe.domain.entities.UserEntity;
import com.tictactoe.springtictactoe.repositories.UserRepository;
import com.tictactoe.springtictactoe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        if (userEntity.getUserId() != null && userRepository.existsById(userEntity.getUserId())) {
            String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
            userEntity.setPassword(encodedPassword);
            return userRepository.save(userEntity);
        }
        return null;
    }

    @Override
    public UserEntity updatePartialUser(Long id, Map<String, Object> updates) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) {
            return null;
        }
        UserEntity user = userOptional.get();
        updates.forEach((key, value) -> {
            switch (key) {
                case "username":
                    user.setUsername((String) value);
                    break;
                case "password":
                    String encodedPassword = passwordEncoder.encode((String) value);
                    user.setPassword(encodedPassword);
                    break;
            }
        });
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
