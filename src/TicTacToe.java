import java.util.Scanner;

public class TicTacToe extends AbstractGame {
    private boolean gameOver;
    private Team winner;
    private InputHandler inputHandler;

    public TicTacToe() {
        this.gameOver = false;
        this.winner = null;
        this.inputHandler = new InputHandler(); // Initialize the input handler
    }


    @Override
    public void play() {
        while (!gameOver) {
            board.display();
            Team currentTeam = getCurrentTeam();
            System.out.println(currentTeam.getName() + "'s turn");

            // Get coordinates
            int row = inputHandler.getIntInput("Enter row (1-" + board.getSize() + "): ", 1, board.getSize()) - 1;
            int col = inputHandler.getIntInput("Enter column (1-" + board.getSize() + "): ", 1, board.getSize()) - 1;

            // Place the piece in the board
            Piece currentPiece = currentTeam.getPiece();
            if (board.placeSymbol(row, col, currentPiece)) {
                if (checkWin(currentPiece.getSymbol())) { // Check win condition using the piece's symbol
                    winner = currentTeam; //  declare winner
                    board.display();
                    System.out.println(currentTeam.getName() + " wins with " + currentPiece.getSymbol() + "!");
                    gameOver = true;
                } else if (board.isFull()) {
                    board.display();
                    System.out.println("It's a tie!");
                    gameOver = true;
                } else {
                    switchPlayer(); // Switches the player
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
        return checkLine(symbol, 0, 0, 1, 1) || checkLine(symbol, 0, board.getSize() - 1, 1, -1);
    }

    private boolean checkLine(String symbol, int startRow, int startCol, int rowIncrement, int colIncrement) {
        for (int i = 0; i < board.getSize(); i++) {
            if (board.getTile(startRow + i * rowIncrement, startCol + i * colIncrement) != symbol.charAt(0)) {
                return false;
            }
        }
        return true;
    }
}
