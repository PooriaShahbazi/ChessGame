package com.example.chess;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardPanel extends JPanel implements MouseListener {

    // Board constants
    private final int TILE_SIZE = 80;       // Size of each square (pixels)
    private final int BOARD_SIZE = 8;       // 8x8 board

    // Extra margins for labels
    private final int MARGIN_LEFT = 30;     // Left margin for row labels
    private final int MARGIN_BOTTOM = 30;   // Bottom margin for column labels

    private Piece[][] board;                // The chess board state
    private java.util.HashMap<String, Image> pieceImages;  // Cache for piece images

    // Variables for tracking human (White) piece selection
    private Piece selectedPiece = null;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<Point> possibleMoves;      // Valid moves for selected piece

    // Turn tracking: "white" (human) or "black" (computer). White starts.
    private String currentTurn = "white";

    // Field to store the king's position (column, row) if in check.
    private Point kingInCheckPos = null;

    public BoardPanel() {
        // Set overall preferred size (board area plus margins)
        setPreferredSize(new Dimension(
                MARGIN_LEFT + TILE_SIZE * BOARD_SIZE, 
                TILE_SIZE * BOARD_SIZE + MARGIN_BOTTOM));
        addMouseListener(this);
        pieceImages = new java.util.HashMap<>();
        loadImages();
        initBoard();
        possibleMoves = new ArrayList<>();
    }

    // Loads piece images from the classpath.
    private void loadImages() {
        pieceImages.put("white_pawn", new ImageIcon(getClass().getResource("/images/white_pawn.png")).getImage());
        pieceImages.put("black_pawn", new ImageIcon(getClass().getResource("/images/black_pawn.png")).getImage());
        pieceImages.put("white_rook", new ImageIcon(getClass().getResource("/images/white_rook.png")).getImage());
        pieceImages.put("black_rook", new ImageIcon(getClass().getResource("/images/black_rook.png")).getImage());
        pieceImages.put("white_knight", new ImageIcon(getClass().getResource("/images/white_knight.png")).getImage());
        pieceImages.put("black_knight", new ImageIcon(getClass().getResource("/images/black_knight.png")).getImage());
        pieceImages.put("white_bishop", new ImageIcon(getClass().getResource("/images/white_bishop.png")).getImage());
        pieceImages.put("black_bishop", new ImageIcon(getClass().getResource("/images/black_bishop.png")).getImage());
        pieceImages.put("white_queen", new ImageIcon(getClass().getResource("/images/white_queen.png")).getImage());
        pieceImages.put("black_queen", new ImageIcon(getClass().getResource("/images/black_queen.png")).getImage());
        pieceImages.put("white_king", new ImageIcon(getClass().getResource("/images/white_king.png")).getImage());
        pieceImages.put("black_king", new ImageIcon(getClass().getResource("/images/black_king.png")).getImage());
    }

    // Initializes the board with the standard chess starting position.
    private void initBoard() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];

        // Pawns
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[6][col] = new Pawn("white", 6, col);
            board[1][col] = new Pawn("black", 1, col);
        }

        // Rooks
        board[7][0] = new Rook("white", 7, 0);
        board[7][7] = new Rook("white", 7, 7);
        board[0][0] = new Rook("black", 0, 0);
        board[0][7] = new Rook("black", 0, 7);

        // Knights
        board[7][1] = new Knight("white", 7, 1);
        board[7][6] = new Knight("white", 7, 6);
        board[0][1] = new Knight("black", 0, 1);
        board[0][6] = new Knight("black", 0, 6);

        // Bishops
        board[7][2] = new Bishop("white", 7, 2);
        board[7][5] = new Bishop("white", 7, 5);
        board[0][2] = new Bishop("black", 0, 2);
        board[0][5] = new Bishop("black", 0, 5);

        // Queens
        board[7][3] = new Queen("white", 7, 3);
        board[0][3] = new Queen("black", 0, 3);

        // Kings
        board[7][4] = new King("white", 7, 4);
        board[0][4] = new King("black", 0, 4);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Draw board squares (with an x-offset for left margin).
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int x = MARGIN_LEFT + col * TILE_SIZE;
                int y = row * TILE_SIZE;
                g.setColor(((row + col) % 2 == 0) ? Color.WHITE : Color.LIGHT_GRAY);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }

        // 2. Highlight possible moves (if a piece is selected).
        if (selectedPiece != null && possibleMoves != null) {
            g.setColor(new Color(0, 255, 0, 128)); // Semi-transparent green
            for (Point move : possibleMoves) {
                int x = MARGIN_LEFT + move.x * TILE_SIZE;
                int y = move.y * TILE_SIZE;
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }

        // 3. Draw the pieces.
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    String key = piece.getColor() + "_" + piece.getType();
                    Image img = pieceImages.get(key);
                    if (img != null) {
                        int x = MARGIN_LEFT + col * TILE_SIZE;
                        int y = row * TILE_SIZE;
                        g.drawImage(img, x, y, TILE_SIZE, TILE_SIZE, this);
                    }
                }
            }
        }

        // 4. Highlight the selected square.
        if (selectedPiece != null) {
            int x = MARGIN_LEFT + selectedCol * TILE_SIZE;
            int y = selectedRow * TILE_SIZE;
            g.setColor(new Color(255, 255, 0, 128)); // Semi-transparent yellow
            g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        }

        // 5. Draw row labels (ranks) in the left margin in dark gray.
        g.setColor(Color.DARK_GRAY);
        for (int row = 0; row < BOARD_SIZE; row++) {
            String label = String.valueOf(BOARD_SIZE - row);
            int labelWidth = g.getFontMetrics().stringWidth(label);
            int x = (MARGIN_LEFT - labelWidth) / 2;
            int y = row * TILE_SIZE + TILE_SIZE / 2 + g.getFontMetrics().getAscent() / 2;
            g.drawString(label, x, y);
        }

        // 6. Draw column labels (files) along the bottom margin in dark gray.
        for (int col = 0; col < BOARD_SIZE; col++) {
            char letter = (char) ('a' + col);
            String label = String.valueOf(letter);
            int labelWidth = g.getFontMetrics().stringWidth(label);
            int x = MARGIN_LEFT + col * TILE_SIZE + (TILE_SIZE - labelWidth) / 2;
            int y = TILE_SIZE * BOARD_SIZE + (MARGIN_BOTTOM + g.getFontMetrics().getAscent()) / 2;
            g.drawString(label, x, y);
        }

        // 7. If the king is in check, highlight its square with a semi-transparent red overlay.
        if (kingInCheckPos != null) {
            int x = MARGIN_LEFT + kingInCheckPos.x * TILE_SIZE;
            int y = kingInCheckPos.y * TILE_SIZE;
            g.setColor(new Color(255, 0, 0, 128)); // Semi-transparent red
            g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        // Process clicks only within the board area.
        if (x < MARGIN_LEFT || x >= MARGIN_LEFT + TILE_SIZE * BOARD_SIZE ||
            y < 0 || y >= TILE_SIZE * BOARD_SIZE) {
            return;
        }
        int col = (x - MARGIN_LEFT) / TILE_SIZE;
        int row = y / TILE_SIZE;

        // Human (White) move handling.
        if (selectedPiece == null) {
            if (board[row][col] != null && board[row][col].getColor().equals(currentTurn)) {
                selectedPiece = board[row][col];
                selectedRow = row;
                selectedCol = col;
                calculatePossibleMoves();
            }
        } else {
            boolean validDestination = false;
            for (Point move : possibleMoves) {
                if (move.x == col && move.y == row) {
                    validDestination = true;
                    break;
                }
            }
            if (validDestination && selectedPiece.isValidMove(row, col, board)) {
                if (board[row][col] != null && board[row][col].getColor().equals(currentTurn)) {
                    selectedPiece = board[row][col];
                    selectedRow = row;
                    selectedCol = col;
                    calculatePossibleMoves();
                } else {
                    board[row][col] = selectedPiece;
                    board[selectedRow][selectedCol] = null;
                    selectedPiece.setPosition(row, col);

                    // Switch turn to Black.
                    currentTurn = currentTurn.equals("white") ? "black" : "white";
                    selectedPiece = null;
                    possibleMoves.clear();
                    checkGameOver();
                    checkForCheck();
                    repaint();

                    // If it's now Black's turn, schedule the computer's move.
                    if (currentTurn.equals("black")) {
                        Timer timer = new Timer(1000, ev -> doSmartComputerMove());
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            } else {
                selectedPiece = null;
                possibleMoves.clear();
            }
        }
        repaint();
    }

    private void calculatePossibleMoves() {
        possibleMoves.clear();
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (selectedPiece.isValidMove(r, c, board)) {
                    Piece target = board[r][c];
                    if (target == null || !target.getColor().equals(selectedPiece.getColor())) {
                        possibleMoves.add(new Point(c, r));
                    }
                }
            }
        }
    }

    private void checkGameOver() {
        boolean whiteKingFound = false;
        boolean blackKingFound = false;
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[r][c] != null && board[r][c].getType().equals("king")) {
                    if (board[r][c].getColor().equals("white"))
                        whiteKingFound = true;
                    else if (board[r][c].getColor().equals("black"))
                        blackKingFound = true;
                }
            }
        }
        if (!whiteKingFound || !blackKingFound) {
            String winner = whiteKingFound ? "White" : "Black";
            JOptionPane.showMessageDialog(this, winner + " wins! Game over.");
            initBoard();
            currentTurn = "white";
            selectedPiece = null;
            possibleMoves.clear();
            repaint();
        }
    }

    // ---------- Methods for Check and Checkmate Detection ----------

    // Determines whether the king of the given color is in check.
    private boolean isInCheck(String color, Piece[][] boardState) {
        int kingRow = -1;
        int kingCol = -1;
        // Find the king's position.
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece p = boardState[i][j];
                if (p != null && p.getType().equals("king") && p.getColor().equals(color)) {
                    kingRow = i;
                    kingCol = j;
                    break;
                }
            }
            if (kingRow != -1) break;
        }
        if (kingRow == -1) return true; // King not found; treat as in check.

        String opponentColor = color.equals("white") ? "black" : "white";
        // Check if any opponent piece can move to the king's position.
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece p = boardState[i][j];
                if (p != null && p.getColor().equals(opponentColor)) {
                    if (p.isValidMove(kingRow, kingCol, boardState)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Determines whether the player of the given color is checkmated.
    private boolean isCheckmate(String color) {
        if (!isInCheck(color, board)) return false;
        List<Move> moves = getAllLegalMoves(board, color);
        for (Move move : moves) {
            Piece[][] newBoard = simulateMove(board, move);
            if (!isInCheck(color, newBoard)) {
                return false;
            }
        }
        return true;
    }

    // Instead of showing a "Check!" message, we store the king's position to highlight it.
    private void checkForCheck() {
        kingInCheckPos = null; // Clear any previous highlight.
        if (isInCheck(currentTurn, board)) {
            if (isCheckmate(currentTurn)) {
                JOptionPane.showMessageDialog(this, "Checkmate! " +
                        (currentTurn.equals("white") ? "Black wins!" : "White wins!"));
                initBoard();
                currentTurn = "white";
                selectedPiece = null;
                possibleMoves.clear();
            } else {
                // Locate the king's position for the current turn.
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        Piece p = board[i][j];
                        if (p != null && p.getType().equals("king") && p.getColor().equals(currentTurn)) {
                            kingInCheckPos = new Point(j, i); // x = column, y = row.
                            break;
                        }
                    }
                    if (kingInCheckPos != null) break;
                }
            }
        }
    }

    // ---------- AI Methods for a "Smart" Computer Move ----------

    private int getPieceValue(Piece piece) {
        switch (piece.getType()) {
            case "pawn":   return 100;
            case "knight": return 320;
            case "bishop": return 330;
            case "rook":   return 500;
            case "queen":  return 900;
            case "king":   return 20000;
            default:       return 0;
        }
    }

    private int evaluateBoard(Piece[][] boardState) {
        int score = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (boardState[i][j] != null) {
                    int value = getPieceValue(boardState[i][j]);
                    score += boardState[i][j].getColor().equals("white") ? value : -value;
                }
            }
        }
        return score;
    }

    private Piece[][] cloneBoard(Piece[][] boardState) {
        Piece[][] newBoard = new Piece[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (boardState[i][j] != null) {
                    newBoard[i][j] = clonePiece(boardState[i][j]);
                }
            }
        }
        return newBoard;
    }

    private Piece clonePiece(Piece p) {
        String color = p.getColor();
        int row = p.getRow();
        int col = p.getCol();
        switch (p.getType()) {
            case "pawn":   return new Pawn(color, row, col);
            case "rook":   return new Rook(color, row, col);
            case "knight": return new Knight(color, row, col);
            case "bishop": return new Bishop(color, row, col);
            case "queen":  return new Queen(color, row, col);
            case "king":   return new King(color, row, col);
            default:       return null;
        }
    }

    private Piece[][] simulateMove(Piece[][] boardState, Move move) {
        Piece[][] newBoard = cloneBoard(boardState);
        Piece movingPiece = newBoard[move.fromRow][move.fromCol];
        newBoard[move.toRow][move.toCol] = movingPiece;
        newBoard[move.fromRow][move.fromCol] = null;
        movingPiece.setPosition(move.toRow, move.toCol);
        return newBoard;
    }

    private List<Move> getAllLegalMoves(Piece[][] boardState, String color) {
        List<Move> moves = new ArrayList<>();
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece p = boardState[r][c];
                if (p != null && p.getColor().equals(color)) {
                    for (int r2 = 0; r2 < BOARD_SIZE; r2++) {
                        for (int c2 = 0; c2 < BOARD_SIZE; c2++) {
                            if (p.isValidMove(r2, c2, boardState)) {
                                Piece target = boardState[r2][c2];
                                if (target == null || !target.getColor().equals(color)) {
                                    moves.add(new Move(r, c, r2, c2));
                                }
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    private boolean isGameOver(Piece[][] boardState) {
        boolean whiteKingFound = false;
        boolean blackKingFound = false;
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (boardState[r][c] != null && boardState[r][c].getType().equals("king")) {
                    if (boardState[r][c].getColor().equals("white"))
                        whiteKingFound = true;
                    else if (boardState[r][c].getColor().equals("black"))
                        blackKingFound = true;
                }
            }
        }
        return (!whiteKingFound || !blackKingFound);
    }

    private int minimax(Piece[][] boardState, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || isGameOver(boardState)) {
            return evaluateBoard(boardState);
        }
        if (maximizingPlayer) {  // White's turn (maximizing)
            int maxEval = Integer.MIN_VALUE;
            List<Move> moves = getAllLegalMoves(boardState, "white");
            for (Move move : moves) {
                Piece[][] newBoard = simulateMove(boardState, move);
                int eval = minimax(newBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {  // Black's turn (minimizing)
            int minEval = Integer.MAX_VALUE;
            List<Move> moves = getAllLegalMoves(boardState, "black");
            for (Move move : moves) {
                Piece[][] newBoard = simulateMove(boardState, move);
                int eval = minimax(newBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private void doSmartComputerMove() {
        List<Move> moves = getAllLegalMoves(board, "black");
        if (moves.isEmpty()) {
            currentTurn = "white";
            repaint();
            return;
        }
        Move bestMove = null;
        int bestEval = Integer.MAX_VALUE; // Black is minimizing.
        int searchDepth = 3; // Adjust depth as needed.
        for (Move move : moves) {
            Piece[][] newBoard = simulateMove(board, move);
            int eval = minimax(newBoard, searchDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            if (eval < bestEval) {
                bestEval = eval;
                bestMove = move;
            }
        }
        if (bestMove != null) {
            Piece movingPiece = board[bestMove.fromRow][bestMove.fromCol];
            board[bestMove.toRow][bestMove.toCol] = movingPiece;
            board[bestMove.fromRow][bestMove.fromCol] = null;
            movingPiece.setPosition(bestMove.toRow, bestMove.toCol);
        }
        currentTurn = "white";
        checkGameOver();
        checkForCheck();
        repaint();
    }

    // Inner class to represent a move.
    private class Move {
        int fromRow, fromCol, toRow, toCol;
        Move(int fromRow, int fromCol, int toRow, int toCol) {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
    }

    // Unused mouse event methods.
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}