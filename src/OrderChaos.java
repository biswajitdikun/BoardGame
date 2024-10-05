import java.util.Scanner;

public class OrderChaos extends AbstractGame {
    private boolean gameOver;
    private Team winner;
    private InputHandler inputHandler;

    public OrderChaos() {
        this.gameOver = false;
        this.winner = null;
        this.inputHandler = new InputHandler(); // Initializes the inputhandler
    }

    @Override
    public void play() {
        while (!gameOver) {
            board.display();
            Team currentTeam = getCurrentTeam();
            System.out.println(currentTeam.getName() + "'s turn");

            // User can choose either X or O
            String symbol = inputHandler.getStringInput("Choose your symbol (X or O): ", "x", "o").toUpperCase(); // Lowercase for easier comparison
            Piece piece = new Piece(symbol.toUpperCase()); // Store as upper case in piece


            int row = inputHandler.getIntInput("Enter row (1-" + board.getSize() + "): ", 1, board.getSize()) - 1;
            int col = inputHandler.getIntInput("Enter column (1-" + board.getSize() + "): ", 1, board.getSize()) - 1;

            // Place the symbol
            if (board.placeSymbol(row, col, piece)) {
                if (checkWin(symbol)) {      //Continuous win check condition
                    winner = currentTeam;
                    board.display();
                    System.out.println(currentTeam.getName() + " wins !");
                    gameOver = true;
                } else if (board.isFull()) {
                    board.display();
                    System.out.println("It's a tie! Chaos wins!");
                    gameOver = true;
                } else {
                    switchPlayer(); // Switch's the player
                }
            } else {
                System.out.println("That position is already taken. Try again.");
            }
        }
    }


    @Override
    public Team getWinner() {
        return winner;
    }

    private boolean checkWin(String symbol) {
        return checkHorizontal(symbol) || checkVertical(symbol) || checkDiagonal(symbol);
    }

    private boolean checkHorizontal(String symbol) {
        for (int i = 0; i < board.getSize(); i++) {
            if (checkLine(symbol, i, 0, 0, 1)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVertical(String symbol) {
        for (int i = 0; i < board.getSize(); i++) {
            if (checkLine(symbol, 0, i, 1, 0)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonal(String symbol) {
        // Check all diagonals from top-left to bottom-right
        for (int i = 0; i <= board.getSize() - 5; i++) {
            for (int j = 0; j <= board.getSize() - 5; j++) {
                if (checkLine(symbol, i, j, 1, 1)) {
                    return true;
                }
            }
        }

        // Check all diagonals from top-right to bottom-left
        for (int i = 0; i <= board.getSize() - 5; i++) {
            for (int j = 4; j < board.getSize(); j++) {
                if (checkLine(symbol, i, j, 1, -1)) {
                    return true;
                }
            }
        }

        return false;
    }


    private boolean checkLine(String symbol, int startRow, int startCol, int rowIncrement, int colIncrement) {
        for (int i = 0; i < 5; i++) { // Check for 5 in a row
            int srow = startRow + i * rowIncrement;
            int scol = startCol + i * colIncrement;
            if (board.getTile(srow,scol) != symbol.charAt(0)) {
                return false;
            }
        }
        return true;
    }

}
