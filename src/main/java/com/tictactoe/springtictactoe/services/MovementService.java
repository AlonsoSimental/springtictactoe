package com.tictactoe.springtictactoe.services;

import com.tictactoe.springtictactoe.domain.entities.BoardEntity;
import com.tictactoe.springtictactoe.domain.entities.MovementEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovementService {

    MovementEntity createMovement(MovementEntity movementEntity, Long boardId);
    List<MovementEntity> findAllMovements();
    Optional<MovementEntity> findMovementById(Long id);
    List<MovementEntity> findAllMovementsByBoard(BoardEntity board);
    MovementEntity updateMovement(MovementEntity movementEntity);
    MovementEntity updatePartialMovement(Long id, Map<String, Object> updates);
    void deleteMovement(Long id);

}
