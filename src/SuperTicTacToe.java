// Rules:
// User take turns and can place anywhere in the 9X9 board.
// In the smaller board of 3X 3 the same rules apply as to a normal Tic Tac Toe.
// Once a smaller board is won either of the player, the prompt declares the same.
// The smaller board result is internally stored as the winner's symbol
// The smaller board result's continue until and unless result is determined int the larger board.

import java.util.Scanner;

public class SuperTicTacToe extends AbstractGame {
    private boolean gameOver;
    private Team winner;
    private InputHandler inputHandler;
    private char[] smallBoardWinners; // 'X', 'O', or ' ' for each of the 9 sub-boards
    private boolean[] smallBoardFull; // true if sub-board is full
    private int movesCount;

    public SuperTicTacToe() {
        this.gameOver = false;
        this.winner = null;
        this.inputHandler = new InputHandler();
        this.smallBoardWinners = new char[9]; // Initialize 9 sub-boards
        this.smallBoardFull = new boolean[9]; // To track full sub-boards
        this.movesCount = 0;
        for (int i = 0; i < 9; i++) {
            smallBoardWinners[i] = ' '; // No winner initially
            smallBoardFull[i] = false;  // None are full initially
        }
    }

    @Override
    public void play() {
        while (!gameOver) {
            board.superDisplay(); // Display the board with sub-grid layout
            Team currentTeam = getCurrentTeam();
            System.out.println(currentTeam.getName() + "'s turn");

            // Allow the player to make a move anywhere on the 9x9 grid
            int row = inputHandler.getIntInput("Enter Row (1-9): ", 1, 9) - 1;
            int col = inputHandler.getIntInput("Enter Column (1-9): ", 1, 9) - 1;

            // Validate the move and process it
            processMove(row, col, currentTeam);
        }
    }

    @Override
    public Team getWinner() {
        return winner;
    }

    private void processMove(int row, int col, Team currentTeam) {
        Piece currentPiece = currentTeam.getPiece();

        // Check if the tile is already occupied
        if (board.placeSymbol(row, col, currentPiece)) {
            movesCount++;

            // Determine the sub-board index based on the move
            int subBoardIndex = getSubBoardIndex(row, col);

            // Check if the current sub-board has a winner after the move
            if (smallBoardWinners[subBoardIndex] == ' ' && checkSmallBoardWin(row, col, currentPiece.getSymbol())) {
                smallBoardWinners[subBoardIndex] = currentPiece.getSymbol().charAt(0); // Mark sub-board with the winner's symbol
                System.out.println(currentTeam.getName() + " wins sub-board " + (subBoardIndex + 1) + "!");
            }

            // Check if the small board is now full
            if (!smallBoardFull[subBoardIndex] && isSmallBoardFull(subBoardIndex)) {
                smallBoardFull[subBoardIndex] = true;
            }

            // Check if the player has won the large board
            if (checkLargeBoardWin(currentPiece.getSymbol())) {
                board.superDisplay();
                System.out.println(currentTeam.getName() + " wins the game!");
                winner = currentTeam;
                gameOver = true;
            } else if (movesCount >= 81) {
                board.superDisplay();
                System.out.println("It's a draw!");
                gameOver = true;
            } else {
                switchPlayer();
            }
        } else {
            System.out.println("This position is already taken. Please try again.");
        }
    }

    private int getSubBoardIndex(int row, int col) {
        // Determine which sub-board (0-8) the move falls into based on the row and column
        int subRow = row / 3;
        int subCol = col / 3;
        return subRow * 3 + subCol; // 0-8 index for the sub-board
    }

    // Updated checkSmallBoardWin method based on the dynamic row and col of the move
    private boolean checkSmallBoardWin(int row, int col, String symbol) {
        char sym = symbol.charAt(0);

        // Determine the top-left corner of the 3x3 sub-board
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;

        // Check rows and columns in one loop
        for (int i = 0; i < 3; i++) {
            // Check row i and column i within the sub-board
            if ((board.getTile(startRow + i, startCol) == sym && board.getTile(startRow + i, startCol + 1) == sym && board.getTile(startRow + i, startCol + 2) == sym) ||
                    (board.getTile(startRow, startCol + i) == sym && board.getTile(startRow + 1, startCol + i) == sym && board.getTile(startRow + 2, startCol + i) == sym)) {
                return true;
            }
        }

        // Check diagonals within the sub-board
        return (board.getTile(startRow, startCol) == sym && board.getTile(startRow + 1, startCol + 1) == sym && board.getTile(startRow + 2, startCol + 2) == sym) ||
                (board.getTile(startRow, startCol + 2) == sym && board.getTile(startRow + 1, startCol + 1) == sym && board.getTile(startRow + 2, startCol) == sym);
    }


    private boolean checkLargeBoardWin(String symbol) {
        char sym = symbol.charAt(0);

        // Check rows for a win
        for (int i = 0; i < 3; i++) {
            // Check row i and column i
            if ((smallBoardWinners[i * 3] == sym && smallBoardWinners[i * 3 + 1] == sym && smallBoardWinners[i * 3 + 2] == sym) ||
                    (smallBoardWinners[i] == sym && smallBoardWinners[i + 3] == sym && smallBoardWinners[i + 6] == sym)) {
                return true;
            }
        }

        // Check diagonals
        return (smallBoardWinners[0] == sym && smallBoardWinners[4] == sym && smallBoardWinners[8] == sym) ||
                (smallBoardWinners[2] == sym && smallBoardWinners[4] == sym && smallBoardWinners[6] == sym);
    }


    // This method checks for a winning line within a sub-board (3x3 grid)


    private boolean isSmallBoardFull(int subBoardIndex) {
        // Check if every tile in the sub-board is full
        int startRow = (subBoardIndex / 3) * 3;
        int startCol = (subBoardIndex % 3) * 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getTile(startRow + i, startCol + j) == ' ') { // Assuming ' ' is empty
                    return false;
                }
            }
        }
        return true;
    }
}
