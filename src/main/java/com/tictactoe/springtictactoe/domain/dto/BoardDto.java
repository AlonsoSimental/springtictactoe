package com.tictactoe.springtictactoe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {
    private Long boardId;

    private String name;

    private LocalDateTime createdAt;

    private Character currentTurn;

    private Character winner;

    private String state = "---------";  // Estado inicial del juego

    private UserDto userEntity;

}
