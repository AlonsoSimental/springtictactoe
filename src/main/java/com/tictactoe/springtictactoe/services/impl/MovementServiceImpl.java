package com.tictactoe.springtictactoe.services.impl;

import com.tictactoe.springtictactoe.domain.entities.BoardEntity;
import com.tictactoe.springtictactoe.domain.entities.MovementEntity;
import com.tictactoe.springtictactoe.repositories.BoardRepository;
import com.tictactoe.springtictactoe.repositories.MovementRepository;
import com.tictactoe.springtictactoe.services.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MovementServiceImpl implements MovementService {

    private MovementRepository movementRepository;
    private BoardRepository boardRepository;

    @Autowired
    public MovementServiceImpl(MovementRepository movementRepository, BoardRepository boardRepository) {
        this.movementRepository = movementRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    public MovementEntity createMovement(MovementEntity movementEntity, Long boardId) {
        Optional<BoardEntity> board = boardRepository.findById(boardId);
        if(board.isPresent()) {
            movementEntity.setMoveDate(LocalDateTime.now());
            movementEntity.setBoard(board.get());
            return movementRepository.save(movementEntity);
        }else {
            throw new RuntimeException("Board not found with ID: "+boardId);
        }
    }

    @Override
    public List<MovementEntity> findAllMovements() {
        return movementRepository.findAll();
    }

    @Override
    public Optional<MovementEntity> findMovementById(Long id) {
        return movementRepository.findById(id);
    }

    @Override
    public MovementEntity updateMovement(MovementEntity movementEntity) {
        if(movementEntity.getMovementId() != null && movementRepository.existsById(movementEntity.getMovementId())) {
            movementEntity.setMoveDate(LocalDateTime.now());
            movementEntity.setBoard(movementEntity.getBoard());
            return movementRepository.save(movementEntity);
        }
        return null;
    }

    @Override
    public List<MovementEntity> findAllMovementsByBoard(BoardEntity board) {
        return movementRepository.findByBoardOrderByMoveDateDesc(board);
    }

    @Override
    public MovementEntity updatePartialMovement(Long id, Map<String, Object> updates) {
        Optional<MovementEntity> movementOptional = movementRepository.findById(id);
        if(!movementOptional.isPresent()) {
            return null;
        }
        MovementEntity movement = movementOptional.get();
        updates.forEach((key, value) -> {
            switch (key) {
                case "position":
                    movement.setPosition((Integer) value);
                    break;
                case "player":
                    movement.setPlayer(((String) value).charAt(0));
                    break;
            }
        });
        return movementRepository.save(movement);
    }

    @Override
    public void deleteMovement(Long id) {
        movementRepository.deleteById(id);
    }

}
