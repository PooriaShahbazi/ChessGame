package com.example.chess;

public class King extends Piece {

    public King(String color, int row, int col) {
        super(color, "king", row, col);
    }
    
    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);
        
        if (rowDiff <= 1 && colDiff <= 1) {
            return board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(color);
        }
        return false;
    }
}