package com.tictactoe.springtictactoe.controllers;

import com.tictactoe.springtictactoe.domain.dto.BoardDto;
import com.tictactoe.springtictactoe.domain.entities.BoardEntity;
import com.tictactoe.springtictactoe.domain.entities.UserEntity;
import com.tictactoe.springtictactoe.logic.GameLogic;
import com.tictactoe.springtictactoe.services.BoardService;
import com.tictactoe.springtictactoe.mapper.Mapper;
import com.tictactoe.springtictactoe.services.GameService;
import com.tictactoe.springtictactoe.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BoardController {

    private BoardService boardService;
    private Mapper mapper;
    private UserService userService;
    private GameLogic gameLogic;
    private GameService gameService;

    public BoardController(BoardService boardService, Mapper mapper, UserService userService, GameLogic gameLogic, GameService gameService) {
        this.boardService = boardService;
        this.mapper = mapper;
        this.userService = userService;
        this.gameLogic = gameLogic;
        this.gameService = gameService;
    }

    @PostMapping(path = "/boards/{userId}")
    public ResponseEntity<?> createBoard(@RequestBody BoardDto boardDto, @PathVariable Long userId) {
        try{
            BoardEntity newBoard = new BoardEntity();
            newBoard.setName(boardDto.getName());
            BoardEntity savedBoard = boardService.createBoard(newBoard, userId);
            return ResponseEntity.ok(savedBoard);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating board: "+e.getMessage());
        }
    }

    @GetMapping(path = "/boards")
    public ResponseEntity<List<BoardDto>> getAllBoards() {
        List<BoardDto> boards = boardService.findAllBoards().stream()
                .map(mapper::boardToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(boards);
    }

    @GetMapping(path = "/boards/{id}")
    public ResponseEntity<BoardDto> findBoardById(@PathVariable Long id) {
        return boardService.findBoardById(id)
                .map(board -> ResponseEntity.ok(mapper.boardToDto(board)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/boards/user/{userId}")
    public ResponseEntity<List<BoardDto>> findAllBoardsByUser(@PathVariable Long userId) { //Todos los boards relacionados a un userId
        Optional<UserEntity> optionalUser = userService.findUserById(userId);
        if(!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            UserEntity user = optionalUser.get();
            List<BoardEntity> boards = boardService.findAllBoardsByUser(user);
            List<BoardDto> boardDtos = boards.stream()
                    .map(mapper::boardToDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(boardDtos);
        }
    }

    @PutMapping(path = "/boards/{id}")
    public ResponseEntity<BoardDto> updateBoard(@PathVariable Long id, @RequestBody BoardDto boardDto) {
        boardDto.setBoardId(id);
        BoardEntity boardEntity = mapper.boardToEntity(boardDto);
        BoardEntity updatedBoard = boardService.updateBoard(boardEntity);
        if(updatedBoard == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.boardToDto(updatedBoard));
    }

    @PatchMapping(path = "/boards/{id}")
    public ResponseEntity<BoardEntity> updatePartialBoard(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        BoardEntity updatedBoard = boardService.updatePartialBoard(id, updates);
        if(updatedBoard == null) {
            return ResponseEntity.notFound().build();
        }
        char winnerState = gameLogic.checkWinner(updatedBoard.getState());
        if(winnerState != '\0') {
            return ResponseEntity.ok(updatedBoard);
        }
        BoardEntity cpuBoard = gameService.cpuMovement(id);
        if (cpuBoard == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(cpuBoard);
    }

    @DeleteMapping(path = "/boards/{boardId}")
    public ResponseEntity<BoardEntity> deleteBoard(@PathVariable Long boardId) {
        Optional<BoardEntity> boardOptional = boardService.findBoardById(boardId);
        if(!boardOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }

}
