package com.example.chess;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class ChessGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            BoardPanel boardPanel = new BoardPanel();
            frame.add(boardPanel);
            frame.pack();
            
            // Disable resizing of the window
            frame.setResizable(false);
            
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);
        });
    }
}