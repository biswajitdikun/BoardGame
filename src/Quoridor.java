import java.util.ArrayList;

import java.util.List;

public class Quoridor extends AbstractGame {
    private boolean gameOver = false;
    private Team winner = null;
    private InputHandler inputHandler = new InputHandler();
    private int[] pawnPositions;
    private List<Wall> walls;
    private int[] wallCounts;
    private int BOARD_SIZE;
    private static final int INITIAL_WALL_COUNT = 10;
    private String symbol1, symbol2;

    //Board
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";

    // Board characters with fixed spacing
    private static final String HORIZONTAL_WALL = "═══";
    private static final String VERTICAL_WALL = "┆";

    private static final String TOP_LEFT = "╔";
    private static final String TOP_RIGHT = "╗";
    private static final String BOTTOM_LEFT = "╚";
    private static final String BOTTOM_RIGHT = "╝";
    private static final String T_TOP = "╦";
    private static final String T_BOTTOM = "╩";
    private static final String T_LEFT = "╠";
    private static final String T_RIGHT = "╣";

    public Quoridor() {
    }

    @Override
    public void configure(GameConfiguration config) {
        super.configure(config);
        initializeGame();
    }

    private void initializeGame() {
        BOARD_SIZE = board.getSize();
        pawnPositions = new int[]{BOARD_SIZE / 2, BOARD_SIZE * (BOARD_SIZE - 1) + BOARD_SIZE / 2};
        walls = new ArrayList<>();
        wallCounts = new int[]{INITIAL_WALL_COUNT, INITIAL_WALL_COUNT};
        symbol1 = getCurrentTeam().getPiece().getSymbol();
        switchPlayer();
        symbol2 = getCurrentTeam().getPiece().getSymbol();
        switchPlayer();
    }

    @Override
    public void play() {

        while (!gameOver) {
            displayBoard();
            Team currentTeam = getCurrentTeam();
            System.out.println(currentTeam.getName() + "'s turn");

            String action = inputHandler.getStringInput("Move pawn (M) or place wall (W)? ", "M", "W", "m", "w");

            if (action.equalsIgnoreCase("M")) {
                movePawn(currentTeam);
            } else {
                placeWall(currentTeam);
            }

            if (checkWin(currentTeam)) {
                winner = currentTeam;
                displayBoard();
                System.out.println(currentTeam.getName() + " wins!");
                gameOver = true;
            } else {
                switchPlayer();
            }
        }
    }


    private void movePawn(Team team) {
        int playerIndex = currentPlayerIndex;
        int currentPos = pawnPositions[playerIndex];
        List<Integer> possibleMoves = getPossibleMoves(currentPos);

        // Convert moves to (row, col) coordinates for display
        List<String> moveCoordinates = convertMovesToCoordinates(possibleMoves);

        System.out.println("Possible moves: " + moveCoordinates);
        int move = inputHandler.getIntInput("Enter your move (0-" + (moveCoordinates.size() - 1) + "): ", 0, moveCoordinates.size() - 1);

        pawnPositions[playerIndex] = possibleMoves.get(move);
    }

    private List<String> convertMovesToCoordinates(List<Integer> moves) {
        List<String> coordinates = new ArrayList<>();
        for (int move : moves) {
            int row = move / BOARD_SIZE;
            int col = move % BOARD_SIZE;
            coordinates.add("(" + row + "," + col + ")");
        }
        return coordinates;
    }

    private void placeWall(Team team) {
        int playerIndex = currentPlayerIndex;
        if (wallCounts[playerIndex] == 0) {
            System.out.println("No walls left. You must move your pawn.");
            movePawn(team);
            return;
        }

        int row = inputHandler.getIntInput("Enter wall row (0-7): ", 0, 7);
        int col = inputHandler.getIntInput("Enter wall column (0-7): ", 0, 7);
        String orientation = inputHandler.getStringInput("Enter wall orientation (H/V): ", "H", "V", "h", "v").toUpperCase();

        Wall newWall = new Wall(row, col, orientation.equals("H"));

        if (isValidWallPlacement(newWall)) {
            walls.add(newWall);
            wallCounts[playerIndex]--;
        } else {
            System.out.println("Invalid wall placement. Try again.");
            placeWall(team);
        }
    }

    private List<Integer> getPossibleMoves(int position) {
        List<Integer> moves = new ArrayList<>();
        int row = position / BOARD_SIZE;
        int col = position % BOARD_SIZE;

        // Check four directions
        checkMove(moves, row - 1, col, position - BOARD_SIZE);
        checkMove(moves, row + 1, col, position + BOARD_SIZE);
        checkMove(moves, row, col - 1, position - 1);
        checkMove(moves, row, col + 1, position + 1);

        // Handle diagonal moves
        List<Integer> diagonalMoves = new ArrayList<>();
        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i) < 0) {
                int blockedPosition = -moves.get(i);
                int blockedRow = blockedPosition / BOARD_SIZE;
                int blockedCol = blockedPosition % BOARD_SIZE;
                checkDiagonalMoves(diagonalMoves, blockedRow, blockedCol);
                moves.remove(i);
                i--;
            }
        }
        moves.addAll(diagonalMoves);

        return moves;
    }

    private void checkMove(List<Integer> moves, int newRow, int newCol, int newPosition) {
        if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE) {
            int currentRow = pawnPositions[currentPlayerIndex] / BOARD_SIZE;
            int currentCol = pawnPositions[currentPlayerIndex] % BOARD_SIZE;
            if (!isWallBetween(currentRow, currentCol, newRow, newCol)) {
                if (newPosition == pawnPositions[1 - currentPlayerIndex]) {
                    // Jump over opponent
                    int jumpRow = newRow + (newRow - pawnPositions[currentPlayerIndex] / BOARD_SIZE);
                    int jumpCol = newCol + (newCol - pawnPositions[currentPlayerIndex] % BOARD_SIZE);
                    int jumpPosition = jumpRow * BOARD_SIZE + jumpCol;
                    if (jumpRow >= 0 && jumpRow < BOARD_SIZE && jumpCol >= 0 && jumpCol < BOARD_SIZE
                            && !isWallBetween(newRow, newCol, jumpRow, jumpCol)) {
                        moves.add(jumpPosition);
                    } else {
                        moves.add(-newPosition); // Blocked position
                    }
                } else {
                    moves.add(newPosition);
                }
            }
        }
    }

    private void checkDiagonalMoves(List<Integer> moves, int blockedRow, int blockedCol) {
        int currentPos = pawnPositions[currentPlayerIndex];
        int currentRow = currentPos / BOARD_SIZE;
        int currentCol = currentPos % BOARD_SIZE;

        int rowDiff = blockedRow - currentRow;
        int colDiff = blockedCol - currentCol;

        int diag1 = (blockedRow * BOARD_SIZE) + (blockedCol - colDiff);
        int diag2 = ((blockedRow - rowDiff) * BOARD_SIZE) + blockedCol;

        if (isValidMove(diag1)) {
            moves.add(diag1);
        }
        if (isValidMove(diag2)) {
            moves.add(diag2);
        }
    }

    private boolean isValidMove(int position) {
        int row = position / BOARD_SIZE;
        int col = position % BOARD_SIZE;

        // Get the current pawn position
        int currentPawnPosition = pawnPositions[currentPlayerIndex];
        int currentPawnRow = currentPawnPosition / BOARD_SIZE;
        int currentPawnCol = currentPawnPosition % BOARD_SIZE;

        // Check for a wall between the current pawn and the target position
        boolean wallBetween = isWallBetween(currentPawnRow, currentPawnCol, row, col);

        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE
                && !wallBetween
                && position != pawnPositions[1 - currentPlayerIndex];
    }


    private boolean isWallBetween(int row1, int col1, int row2, int col2) {
        for (Wall wall : walls) {
            if (wall.checkHorizontal()) {
                // Horizontal wall check
                if ((row1 == wall.getRow() && row2 == wall.getRow() + 1) ||
                        (row2 == wall.getRow() && row1 == wall.getRow() + 1)) {
                    if (col1 >= wall.getCol() && col1 < wall.getCol() + 2) return true;
                }
            } else {
                // Vertical wall check
                if (row1 == row2) { // Moving in same row
                    int minCol = Math.min(col1, col2);
                    int maxCol = Math.max(col1, col2);
                    if (wall.getCol() > minCol && wall.getCol() <= maxCol &&
                            row1 >= wall.getRow() && row1 < wall.getRow() + 2) {
                        return true;
                    }
                } else { // Moving between rows
                    if ((col1 == wall.getCol() && col2 == wall.getCol() + 1) ||
                            (col2 == wall.getCol() && col1 == wall.getCol() + 1)) {
                        if (row1 >= wall.getRow() && row1 < wall.getRow() + 2) return true;
                    }
                }
            }
        }
        return false;
    }


    private boolean isValidWallPlacement(Wall wall) {
        // Check if the wall is within the board boundaries
        if (wall.getRow() < 0 || wall.getRow() > 7 || wall.getCol() < 0 || wall.getCol() > 7) {
            return false;
        }

        // Check for collision with existing walls
        for (Wall existingWall : walls) {
            if (existingWall.getRow() == wall.getRow() && existingWall.getCol() == wall.getCol()) {
                return false;
            }
            if (existingWall.checkHorizontal() == wall.checkHorizontal()) {
                if (existingWall.checkHorizontal()) {
                    if (existingWall.getRow() == wall.getRow() && Math.abs(existingWall.getCol() - wall.getCol()) <= 1) {
                        return false;
                    }
                } else {
                    if (existingWall.getCol() == wall.getCol() && Math.abs(existingWall.getRow() - wall.getRow()) <= 1) {
                        return false;
                    }
                }
            }
        }

        // Temporarily add the wall to check if both players have a path to their goal
        walls.add(wall);
        boolean pathExists = hasPathToGoal(0) && hasPathToGoal(1);
        walls.remove(walls.size() - 1);

        return pathExists;
    }

    private boolean hasPathToGoal(int playerIndex) {
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        int startRow = pawnPositions[playerIndex] / BOARD_SIZE;
        int startCol = pawnPositions[playerIndex] % BOARD_SIZE;
        return dfsPathFinding(startRow, startCol, playerIndex, visited);
    }

    private boolean dfsPathFinding(int row, int col, int playerIndex, boolean[][] visited) {
        if ((playerIndex == 0 && row == BOARD_SIZE - 1) || (playerIndex == 1 && row == 0)) {
            return true;
        }
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || visited[row][col]) {
            return false;
        }

        visited[row][col] = true;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (!isWallBetween(row, col, newRow, newCol) && dfsPathFinding(newRow, newCol, playerIndex, visited)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Team getWinner() {
        return winner;
    }

    private boolean checkWin(Team currentTeam) {
        int playerIndex = currentPlayerIndex;
        int pawnPos = pawnPositions[playerIndex];
        int row = pawnPos / BOARD_SIZE;

        return (playerIndex == 0 && row == BOARD_SIZE - 1) || (playerIndex == 1 && row == 0);
    }

    public void displayBoard() {
        // Column numbers
        System.out.print("  ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(CYAN + " " + i + "  " + RESET);
        }
        System.out.println();

        // Top border
        System.out.print("  " + YELLOW + TOP_LEFT);
        for (int j = 0; j < BOARD_SIZE - 1; j++) {
            System.out.print(HORIZONTAL_WALL + T_TOP);
        }
        System.out.println(HORIZONTAL_WALL + TOP_RIGHT + RESET);

        // Board content
        for (int i = 0; i < BOARD_SIZE; i++) {
            // Row number
            System.out.print(CYAN + i + " " + RESET);

            // Print cells and vertical walls
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Print vertical wall or border
                if (j == 0) {
                    System.out.print(YELLOW + VERTICAL_WALL + RESET);
                } else {
                    boolean hasVerticalWall = hasVerticalWall(i, j);
                    if (hasVerticalWall) {
                        System.out.print(GREEN + "║" + RESET);
                    } else {
                        System.out.print(YELLOW + "┆" + RESET); // Changed to lighter vertical line
                    }
                }

                // Print pawn or empty cell
                int pos = i * BOARD_SIZE + j;
                if (pos == pawnPositions[0]) {
                    System.out.print(" " + RED + symbol1 + " " + RESET);
                } else if (pos == pawnPositions[1]) {
                    System.out.print(" " + BLUE + symbol2 + " " + RESET);
                } else {
                    System.out.print(" · ");
                }
            }
            // Print final vertical wall of the row
            System.out.println(YELLOW + VERTICAL_WALL + RESET);

            // Print horizontal walls between rows
            if (i < BOARD_SIZE - 1) {
                System.out.print("  " + YELLOW + T_LEFT);
                for (int j = 0; j < BOARD_SIZE - 1; j++) {
                    // Check for horizontal wall
                    boolean hasHorizontalWall = hasHorizontalWall(i, j);
                    boolean hasVerticalWall = hasVerticalWall(i, j + 1);
                    boolean hasNextVerticalWall = hasVerticalWall(i + 1, j + 1);

                    if (hasHorizontalWall) {
                        System.out.print(GREEN + HORIZONTAL_WALL + RESET);
                    } else {
                        System.out.print(YELLOW + HORIZONTAL_WALL + RESET);
                    }

                    // Print intersection
                    if (hasVerticalWall || hasNextVerticalWall) {
                        if (hasHorizontalWall) {
                            System.out.print(GREEN + "╬" + RESET);
                        } else {
                            System.out.print(GREEN + "║" + RESET);
                        }
                    } else if (hasHorizontalWall) {
                        System.out.print(GREEN + "═" + RESET);
                    } else {
                        System.out.print(YELLOW + "+" + RESET);
                    }
                }

                // Last horizontal wall in the row
                boolean hasLastHorizontalWall = hasHorizontalWall(i, BOARD_SIZE - 2);
                if (hasLastHorizontalWall) {
                    System.out.print(GREEN + HORIZONTAL_WALL + RESET);
                } else {
                    System.out.print(YELLOW + HORIZONTAL_WALL + RESET);
                }
                System.out.println(YELLOW + T_RIGHT + RESET);
            }
        }

        // Bottom border
        System.out.print("  " + YELLOW + BOTTOM_LEFT);
        for (int j = 0; j < BOARD_SIZE - 1; j++) {
            System.out.print(HORIZONTAL_WALL + T_BOTTOM);
        }
        System.out.println(HORIZONTAL_WALL + BOTTOM_RIGHT + RESET);

        // Game status
        System.out.println("\nWalls remaining:");
        System.out.println(RED + getCurrentTeam().getName() + " (" + symbol1 + "): " + wallCounts[0] + RESET);
        switchPlayer();
        System.out.println(BLUE + getCurrentTeam().getName() + " (" + symbol2 + "): " + wallCounts[1] + RESET);
        switchPlayer();
    }

    // Modified hasVerticalWall method to strictly check for 2-block length
    private boolean hasVerticalWall(int row, int col) {
        for (Wall wall : walls) {
            if (!wall.checkHorizontal() && // Vertical wall
                    wall.getCol() == col && // At wall column
                    row >= wall.getRow() && row <= wall.getRow() + 1) { // Exactly 2 blocks height
                return true;
            }
        }
        return false;
    }

    // Helper method for horizontal walls should remain the same
    private boolean hasHorizontalWall(int row, int col) {
        for (Wall wall : walls) {
            if (wall.checkHorizontal() && // Horizontal wall
                    wall.getRow() == row && // At wall row
                    wall.getCol() <= col && col < wall.getCol() + 2) { // Within wall width
                return true;
            }
        }
        return false;
    }
}