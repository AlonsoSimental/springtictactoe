package com.tictactoe.springtictactoe.domain.dto;

import com.tictactoe.springtictactoe.domain.entities.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovementDto {

    private Long movementId;

    private Integer position;

    private Character player;

    private LocalDateTime moveDate;

    private BoardDto boardEntity;

}
