package com.tictactoe.springtictactoe.controllers;

import com.tictactoe.springtictactoe.domain.dto.BoardDto;
import com.tictactoe.springtictactoe.domain.dto.MovementDto;
import com.tictactoe.springtictactoe.domain.entities.BoardEntity;
import com.tictactoe.springtictactoe.domain.entities.MovementEntity;
import com.tictactoe.springtictactoe.domain.entities.UserEntity;
import com.tictactoe.springtictactoe.mapper.Mapper;
import com.tictactoe.springtictactoe.services.BoardService;
import com.tictactoe.springtictactoe.services.MovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MovementController {

    private MovementService movementService;
    private Mapper mapper;
    private BoardService boardService;

    public MovementController(MovementService movementService, Mapper mapper, BoardService boardService) {
        this.movementService = movementService;
        this.mapper = mapper;
        this.boardService = boardService;
    }

    @PostMapping(path = "/movements/{boardId}")
    public ResponseEntity<?> createMovement(@RequestBody MovementDto movementDto, @PathVariable Long boardId) {
        try {
            MovementEntity newMovement = new MovementEntity();
            newMovement.setPosition(movementDto.getPosition());
            newMovement.setPlayer(movementDto.getPlayer());
            MovementEntity savedMovement = movementService.createMovement(newMovement, boardId);
            return ResponseEntity.ok(savedMovement);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body("Error creating board: "+e.getMessage());
        }
    }

    @GetMapping(path = "/movements")
    public ResponseEntity<List<MovementDto>> getAllMovements() {
        List<MovementDto> movements = movementService.findAllMovements().stream()
                .map(mapper::movementToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movements);
    }

    @GetMapping(path = "/movements/{id}")
    public ResponseEntity<MovementDto> findMovementById(@PathVariable Long id) {
        return movementService.findMovementById(id)
                .map(movement -> ResponseEntity.ok(mapper.movementToDto(movement)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping(path = "/movements/board/{boardId}")
    public ResponseEntity<List<MovementDto>> findAllMovementsByBoard(@PathVariable Long boardId) {
        Optional<BoardEntity> optionalBoard = boardService.findBoardById(boardId);
        if(!optionalBoard.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            BoardEntity board = optionalBoard.get();
            List<MovementEntity> movements = movementService.findAllMovementsByBoard(board);
            List<MovementDto> movementDtos = movements.stream()
                    .map(mapper::movementToDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(movementDtos);
        }
    }

    @PutMapping(path = "/movements/{id}")
    public ResponseEntity<MovementDto> updateMovement(@PathVariable Long id, @RequestBody MovementDto movementDto) {
        movementDto.setMovementId(id);
        MovementEntity movementEntity = mapper.movementToEntity(movementDto);
        MovementEntity updatedMovement = movementService.updateMovement(movementEntity);
        if(updatedMovement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.movementToDto(updatedMovement));
    }

    @PatchMapping(path = "/movements/{id}")
    public ResponseEntity<MovementEntity> updatePartialMovement(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        MovementEntity updatedMovement = movementService.updatePartialMovement(id, updates);
        if(updatedMovement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMovement);
    }

    @DeleteMapping(path = "/movements/{id}")
    public ResponseEntity<MovementEntity> deleteMovement(@PathVariable Long id) {
        Optional<MovementEntity> movementOptional = movementService.findMovementById(id);
        if(!movementOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        movementService.deleteMovement(id);
        return ResponseEntity.ok().build();
    }

}
