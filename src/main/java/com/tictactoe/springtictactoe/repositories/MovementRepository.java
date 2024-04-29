package com.tictactoe.springtictactoe.repositories;

import com.tictactoe.springtictactoe.domain.entities.BoardEntity;
import com.tictactoe.springtictactoe.domain.entities.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementRepository extends JpaRepository<MovementEntity, Long> {


    List<MovementEntity> findByBoardOrderByMoveDateDesc(BoardEntity board);

}
