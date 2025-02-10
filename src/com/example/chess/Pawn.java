package com.example.chess;

public class Pawn extends Piece {

    public Pawn(String color, int row, int col) {
        super(color, "pawn", row, col);
    }
    
    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int direction = color.equals("white") ? -1 : 1;  // White moves up, black moves down
        
        // Check bounds.
        if (newRow < 0 || newRow >= board.length || newCol < 0 || newCol >= board[0].length) {
            return false;
        }
        
        int startRow = color.equals("white") ? 6 : 1;
        
        // Move forward.
        if (newCol == col) {
            if (newRow == row + direction && board[newRow][newCol] == null) {
                return true;
            }
            if (row == startRow && newRow == row + 2 * direction &&
                board[row + direction][newCol] == null && board[newRow][newCol] == null) {
                return true;
            }
        }
        
        // Diagonal capture.
        if (Math.abs(newCol - col) == 1 && newRow == row + direction) {
            if (board[newRow][newCol] != null && !board[newRow][newCol].getColor().equals(color)) {
                return true;
            }
        }
        
        return false;
    }
}