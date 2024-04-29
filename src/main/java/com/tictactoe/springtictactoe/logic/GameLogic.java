package com.tictactoe.springtictactoe.logic;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class GameLogic {

    public String findBestMove(String currentState) {
        char[] board = currentState.toCharArray();
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        List<Integer> openPositions = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            if(board[i] == '-') {
                openPositions.add(i);
                board[i] = 'O';
                int score = minimax(board, 0, false);
                board[i] = '-';

                if(score>bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if(bestMove == -1 && !openPositions.isEmpty()) {
            Random rand = new Random();
            bestMove = openPositions.get(rand.nextInt(openPositions.size()));
        }
        if(bestMove != -1) {
            board[bestMove] = 'O';
            return new String(board);
        }
        return currentState;

    }

    public int minimax(char[] state, int depth, boolean isMaximizing) {
        char winner = checkWinner(new String(state));
        if (winner != '\0') {
            return winner == 'O' ? 10 - depth : depth - 10;
        }

        if (isFull(state)) {
            return 0;
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < state.length; i++) {
            if (state[i] == '-') {
                state[i] = isMaximizing ? 'O' : 'X';
                int score = minimax(state, depth + 1, !isMaximizing);
                state[i] = '-';
                bestScore = isMaximizing ? Math.max(score, bestScore) : Math.min(score, bestScore);
            }
        }
        return bestScore;
    }

    private boolean isFull(char[] state) {
        for (char c : state) {
            if (c == '-') {
                return false;
            }
        }
        return true;
    }


    public char checkWinner(String state) {
        char[] board = state.toCharArray();
        // Combinaciones ganadoras en el TicTacToe
        int[][] winningCombinations = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6}
        };
        for (int[] combo : winningCombinations) {
            if (board[combo[0]] != '-' &&
                    board[combo[0]] == board[combo[1]] &&
                    board[combo[1]] == board[combo[2]]) {
                return board[combo[0]];
            }
        }

        return '\0';
    }


}
