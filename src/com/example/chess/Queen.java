package com.example.chess;

public class Queen extends Piece {

    public Queen(String color, int row, int col) {
        super(color, "queen", row, col);
    }
    
    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        // Straight-line move.
        if (row == newRow || col == newCol) {
            int rowStep = Integer.compare(newRow, row);
            int colStep = Integer.compare(newCol, col);
            int currentRow = row + rowStep;
            int currentCol = col + colStep;
            while (currentRow != newRow || currentCol != newCol) {
                if (board[currentRow][currentCol] != null) {
                    return false;
                }
                currentRow += rowStep;
                currentCol += colStep;
            }
            return board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(color);
        }
        
        // Diagonal move.
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);
        if (rowDiff == colDiff) {
            int rowStep = (newRow > row) ? 1 : -1;
            int colStep = (newCol > col) ? 1 : -1;
            int currentRow = row + rowStep;
            int currentCol = col + colStep;
            while (currentRow != newRow && currentCol != newCol) {
                if (board[currentRow][currentCol] != null) {
                    return false;
                }
                currentRow += rowStep;
                currentCol += colStep;
            }
            return board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(color);
        }
        
        return false;
    }
}