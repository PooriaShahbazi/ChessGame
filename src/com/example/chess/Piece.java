package com.example.chess;

public abstract class Piece {
    protected String color; // "white" or "black"
    protected String type;  // e.g., "pawn", "rook", etc.
    protected int row, col; // Current position on the board

    public Piece(String color, String type, int row, int col) {
        this.color = color;
        this.type = type;
        this.row = row;
        this.col = col;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getType() {
        return type;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    // Updates the piece's position.
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    // Checks if moving to (newRow, newCol) is valid for this piece.
    // The board parameter represents the current board state.
    public abstract boolean isValidMove(int newRow, int newCol, Piece[][] board);
}