package com.tictactoe.springtictactoe.services;

import com.tictactoe.springtictactoe.domain.entities.BoardEntity;
import com.tictactoe.springtictactoe.domain.entities.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BoardService {

    BoardEntity createBoard(BoardEntity boardEntity, Long userId);
    List<BoardEntity> findAllBoards();
    Optional<BoardEntity> findBoardById(Long id);
    List<BoardEntity> findAllBoardsByUser(UserEntity user);

    BoardEntity updateBoard(BoardEntity boardEntity);
    BoardEntity updatePartialBoard(Long id, Map<String, Object> updates);
    void deleteBoard(Long id);

}
