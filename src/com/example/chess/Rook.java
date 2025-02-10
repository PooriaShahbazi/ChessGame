package com.example.chess;

public class Rook extends Piece {

    public Rook(String color, int row, int col) {
        super(color, "rook", row, col);
    }
    
    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        // Must move in a straight line.
        if (row != newRow && col != newCol) {
            return false;
        }
        
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
}