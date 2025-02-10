# ChessGame
Chess Game in Java

This project is a 2D chess game built in Java using the Swing library. It features:
	
•	Graphical User Interface:
A fully drawn chess board with row numbers and column letters. The board does not allow resizing, so the layout remains consistent.
	
•	Standard Chess Mechanics:
All standard pieces are implemented with valid move rules, including special moves like castling (rochade). When castling, both the king and the corresponding rook are moved automatically.
	
•	Visual Feedback:
	•	Possible Moves: When you select a piece, its valid moves are highlighted in semi-transparent green.
	•	Check Indication: Instead of a dialog, the square containing the king in check is highlighted with a semi-transparent red overlay.
	
•	Turn-Based Gameplay:
White is controlled by the user and Black by a basic computer AI. The game enforces alternating turns and ensures pieces cannot be moved to occupied squares by friendly pieces.
	
•	Basic AI for Black:
The computer uses a simple minimax algorithm with alpha–beta pruning to choose its moves based on material evaluation. Although the AI is basic, it provides a functional opponent for casual play.

How to Run
	1.	Clone the Repository:
Clone this repository to your local machine.
	2.	Setup:
Ensure that the image assets (e.g., white_pawn.png, black_king.png, etc.) are located in a folder that is on the classpath (for example, within the src/images directory).
	3.	Compile and Run:
Open the project in Eclipse (or your preferred Java IDE), build the project, and run the ChessGame.java main class.

Enjoy playing chess against a simple AI!
# Visual presentation
![Alt text](img1.png)
![Alt text](img2.png)
