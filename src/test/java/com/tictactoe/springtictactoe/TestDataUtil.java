package com.tictactoe.springtictactoe;

import com.tictactoe.springtictactoe.domain.entities.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDataUtil {

    public UserEntity createTestUser() {
        return UserEntity.builder()
                .userId(1L)
                .username("testUser")
                .password("testPass")
                .build();
    }

}
