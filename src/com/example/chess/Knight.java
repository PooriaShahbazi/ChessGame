package com.example.chess;

public class Knight extends Piece {

    public Knight(String color, int row, int col) {
        super(color, "knight", row, col);
    }
    
    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);
        
        if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
            return false;
        }
        
        return board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(color);
    }
}