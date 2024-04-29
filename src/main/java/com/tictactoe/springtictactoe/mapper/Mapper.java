package com.tictactoe.springtictactoe.mapper;

import com.tictactoe.springtictactoe.domain.dto.BoardDto;
import com.tictactoe.springtictactoe.domain.dto.MovementDto;
import com.tictactoe.springtictactoe.domain.dto.UserDto;
import com.tictactoe.springtictactoe.domain.entities.BoardEntity;
import com.tictactoe.springtictactoe.domain.entities.MovementEntity;
import com.tictactoe.springtictactoe.domain.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class Mapper {

    public UserDto userToDto(UserEntity user) {
        if(user == null) {
            return null;
        }
        return UserDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();
    }

    public UserEntity userToEntity(UserDto userDto) {
        if(userDto==null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setUserId(userDto.getUserId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        return user;
    }

    public BoardDto boardToDto(BoardEntity board) {
        if(board == null) {
            return null;
        }
        return BoardDto.builder()
                .boardId(board.getBoardId())
                .name(board.getName())
                .createdAt(board.getCreatedAt())
                .currentTurn(board.getCurrentTurn())
                .winner(board.getWinner())
                .state(board.getState())
                .userEntity(board.getUser() != null ? userToDto(board.getUser()) : null)
                .build();
    }

    public BoardEntity boardToEntity(BoardDto boardDto) {
        if(boardDto == null) {
            return null;
        }
        BoardEntity board = new BoardEntity();
        board.setBoardId(boardDto.getBoardId());
        board.setName(boardDto.getName());
        board.setCurrentTurn(boardDto.getCurrentTurn());
        board.setWinner(boardDto.getWinner());
        board.setState(boardDto.getState());
        board.setUser(boardDto.getUserEntity() != null ? userToEntity(boardDto.getUserEntity()) : null);
        return board;
    }

    public MovementDto movementToDto(MovementEntity movement) {
        if(movement == null) {
            return null;
        }
        return MovementDto.builder()
                .movementId(movement.getMovementId())
                .position(movement.getPosition())
                .player(movement.getPlayer())
                .moveDate(movement.getMoveDate())
                .boardEntity(movement.getBoard() != null ? boardToDto(movement.getBoard()) : null)
                .build();
    }

    public MovementEntity movementToEntity(MovementDto movementDto) {
        if(movementDto == null) {
            return null;
        }
        MovementEntity movement = new MovementEntity();
        movement.setMovementId(movementDto.getMovementId());
        movement.setPosition(movementDto.getPosition());
        movement.setPlayer(movementDto.getPlayer());
        movement.setMoveDate(movementDto.getMoveDate());
        movement.setBoard(movementDto.getBoardEntity() != null ? boardToEntity(movementDto.getBoardEntity()) : null);
        return movement;
    }

}
