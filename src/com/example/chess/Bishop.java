package com.example.chess;

public class Bishop extends Piece {

    public Bishop(String color, int row, int col) {
        super(color, "bishop", row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);
        // Must move diagonally: row difference equals column difference.
        if (rowDiff != colDiff) {
            return false;
        }

        int rowStep = (newRow > row) ? 1 : -1;
        int colStep = (newCol > col) ? 1 : -1;
        int currentRow = row + rowStep;
        int currentCol = col + colStep;

        // Loop through squares along the diagonal until we reach (newRow, newCol).
        while (currentRow != newRow && currentCol != newCol) {
            // Check boundaries to prevent index -1 or index 8
            if (currentRow < 0 || currentRow >= board.length ||
                currentCol < 0 || currentCol >= board[0].length) {
                return false;
            }
            // If any square in the path is occupied, the move is blocked.
            if (board[currentRow][currentCol] != null) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        
        // Allow the move if the destination square is empty or has an enemy piece.
        return board[newRow][newCol] == null ||
               !board[newRow][newCol].getColor().equals(color);
    }
}