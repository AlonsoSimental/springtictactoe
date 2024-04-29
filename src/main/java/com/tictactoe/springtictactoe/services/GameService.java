package com.tictactoe.springtictactoe.services;

import com.tictactoe.springtictactoe.domain.entities.BoardEntity;
import com.tictactoe.springtictactoe.domain.entities.MovementEntity;
import com.tictactoe.springtictactoe.logic.GameLogic;
import com.tictactoe.springtictactoe.repositories.BoardRepository;
import com.tictactoe.springtictactoe.repositories.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MovementRepository movementRepository;
    @Autowired
    private GameLogic gameLogic;

    public BoardEntity cpuMovement(Long boardId) {
        Optional<BoardEntity> board = boardRepository.findById(boardId);
        if(!board.isPresent()) {
            return null;
        }
        BoardEntity cpuBoard = board.get();
        String oldState = cpuBoard.getState();
        String cpuState = gameLogic.findBestMove(cpuBoard.getState());
        char winner = gameLogic.checkWinner(cpuState);
        cpuBoard.setState(cpuState);
        cpuBoard.setCurrentTurn('X');
        cpuBoard.setWinner(winner == '\0' ? null : winner);
        boardRepository.save(cpuBoard);

        MovementEntity movement = new MovementEntity();
        int moveIndex = calculateMoveIndex(oldState, cpuState);
        movement.setPosition(moveIndex + 1);
        movement.setPlayer('O');
        movement.setMoveDate(LocalDateTime.now());
        movement.setBoard(cpuBoard);
        movementRepository.save(movement);

        return cpuBoard;

    }

    private int calculateMoveIndex(String oldState, String newState) {
        for (int i = 0; i < oldState.length(); i++) {
            if (oldState.charAt(i) != newState.charAt(i)) {
                return i;
            }
        }
        throw new IllegalStateException("No move detected.");
    }

}
