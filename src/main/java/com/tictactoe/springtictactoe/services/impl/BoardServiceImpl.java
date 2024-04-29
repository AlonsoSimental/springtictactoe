package com.tictactoe.springtictactoe.services.impl;

import com.tictactoe.springtictactoe.domain.entities.BoardEntity;
import com.tictactoe.springtictactoe.domain.entities.MovementEntity;
import com.tictactoe.springtictactoe.domain.entities.UserEntity;
import com.tictactoe.springtictactoe.logic.GameLogic;
import com.tictactoe.springtictactoe.repositories.BoardRepository;
import com.tictactoe.springtictactoe.repositories.MovementRepository;
import com.tictactoe.springtictactoe.repositories.UserRepository;
import com.tictactoe.springtictactoe.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    private BoardRepository boardRepository;
    private UserRepository userRepository;
    private MovementRepository movementRepository;
    private GameLogic gameLogic;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, UserRepository userRepository, GameLogic gameLogic, MovementRepository movementRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.gameLogic = gameLogic;
        this.movementRepository = movementRepository;
    }

    @Override
    public BoardEntity createBoard(BoardEntity boardEntity, Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isPresent()) {
            boardEntity.setCreatedAt(LocalDateTime.now());
            boardEntity.setCurrentTurn(Math.random() < 0.5 ? 'X' : 'O');
            boardEntity.setState("---------");
            boardEntity.setWinner(null);
            boardEntity.setUser(user.get());
            return boardRepository.save(boardEntity);
        }else {
            throw new RuntimeException("User not found with ID: "+userId);
        }
    }

    @Override
    public List<BoardEntity> findAllBoards() {
        return boardRepository.findAll();
    }

    @Override
    public Optional<BoardEntity> findBoardById(Long id) {
        return boardRepository.findById(id);
    }

    @Override
    public List<BoardEntity> findAllBoardsByUser(UserEntity user) {
        return boardRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public BoardEntity updateBoard(BoardEntity boardEntity) {
        if (boardEntity.getBoardId() != null && boardRepository.existsById(boardEntity.getBoardId())) {
            boardEntity.setCreatedAt(LocalDateTime.now());
            boardEntity.setState("---------");
            boardEntity.setUser(boardEntity.getUser());
           return boardRepository.save(boardEntity);
        }
        return null;
    }

    @Override
    public BoardEntity updatePartialBoard(Long id, Map<String, Object> updates) {
        Optional<BoardEntity> boardOptional = boardRepository.findById(id);
        if(!boardOptional.isPresent()) {
            return null;
        }
        BoardEntity board = boardOptional.get();
        updates.forEach((key, value) -> {
            switch (key) {
                case "currentTurn":
                    board.setCurrentTurn(((String) value).charAt(0));
                    break;
                case "winner":
                    board.setWinner(((String) value).charAt(0));
                    break;
                case "state":
                    board.setState((String) value);
                    char winner = gameLogic.checkWinner((String) value);
                    board.setWinner(winner != '\0' ? winner : null);
            }
        });
        return boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Long id) {
        Optional<BoardEntity> optionalBoard = boardRepository.findById(id);
        BoardEntity board = optionalBoard.get();
        List<MovementEntity> movements = movementRepository.findByBoardOrderByMoveDateDesc(board);
        movementRepository.deleteAll(movements);

        boardRepository.deleteById(id);
    }

}
