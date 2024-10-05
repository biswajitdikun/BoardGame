class Board {
    private final int size;
    private final Tile[][] tiles;

    public Board(int size) {
        this.size = size;
        this.tiles = new Tile[size][size];
        resetBoard();
    }

    public int getSize() {
        return size;
    }

    public void resetBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i][j] = new Tile(); // Initializes each tile
            }
        }
    }

    public boolean placeSymbol(int row, int col, Piece piece) {
        if (!tiles[row][col].isOccupied()) {
            tiles[row][col].placeSymbol(piece.getSymbol().charAt(0)); // Place piece symbol in the tile
            return true;
        }
        return false;
    }

    public char getTile(int row, int col) {
        return tiles[row][col].getSymbol();
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!tiles[i][j].isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void display() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(" " + tiles[i][j].getSymbol() + " ");
                if (j < size - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i < size - 1) {
                // Separator line
                for (int j = 0; j < size; j++) {
                    System.out.print("---");
                    if (j < size - 1) {
                        System.out.print("+");
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    //Dispaly Super Tic Tac Toe
    public void superDisplay() {
        int subGridSize = 3; // Each small Tic-Tac-Toe is 3x3
        int totalSize = subGridSize * subGridSize; // Total grid is 9x9

        // Loop through rows
        for (int i = 0; i < totalSize; i++) {
            // Print individual row from each subgrid row
            for (int j = 0; j < totalSize; j++) {
                System.out.print(" " + tiles[i][j].getSymbol() + " ");

                // Print vertical separator for subgrids
                if ((j + 1) % subGridSize == 0 && j < totalSize - 1) {
                    System.out.print("||"); // Double separator between subgrids
                } else if (j < totalSize - 1) {
                    System.out.print("|"); // Regular separator within subgrid
                }
            }
            System.out.println();

            // Print horizontal separator between rows
            if ((i + 1) % subGridSize == 0 && i < totalSize - 1) {
                // Double horizontal separator between subgrids
                for (int j = 0; j < totalSize; j++) {
                    System.out.print("==="); // Thicker lines
                    if ((j + 1) % subGridSize == 0 && j < totalSize - 1) {
                        System.out.print("++"); // Thicker intersection points
                    } else if (j < totalSize - 1) {
                        System.out.print("+"); // Regular intersection points
                    }
                }
                System.out.println();
            } else if (i < totalSize - 1) {
                // Regular horizontal separator within subgrids
                for (int j = 0; j < totalSize; j++) {
                    System.out.print("---");
                    if ((j + 1) % subGridSize == 0 && j < totalSize - 1) {
                        System.out.print("||");
                    } else if (j < totalSize - 1) {
                        System.out.print("+");
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
    }
}
